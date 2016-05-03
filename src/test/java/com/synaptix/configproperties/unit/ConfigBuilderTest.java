package com.synaptix.configproperties.unit;

import com.synaptix.configproperties.ConfigBuilder;
import com.synaptix.configproperties.ConfigProperty;
import com.synaptix.configproperties.loader.DefaultConfigLoader;
import com.synaptix.configproperties.loader.LoaderReadException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class ConfigBuilderTest {

    @Test
    public void testConfigProviderRead() {
        ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
        builder.configProperty(ConfigProperty.toString("server.nomade-servlet.url", ConfigFields.nomadeSerlvetUrl, null),
                ConfigProperty.toGeneric("server.service-impl.type", ConfigFields.serviceImplType, IConfig.ServiceImplType::valueOf, IConfig.ServiceImplType.Fake),
                ConfigProperty.toLong("server.max-image-upload-avarie", ConfigFields.maxSizeUploadAvarieImage, 1024L * 1024L /* 1Mo */),
                ConfigProperty.toGeneric("server.public-attachments-path", ConfigFields.publicAttachmentsDirectory, Paths::get, Paths.get("public/attachments/")));
        IConfig config = builder.build();

        BDDAssertions.then(config.getNomadeSerlvetUrl()).isEqualTo("https://nomade.talanlabs.com");
        BDDAssertions.then(config.getServiceImplType()).isEqualTo(IConfig.ServiceImplType.Fake);
        BDDAssertions.then(config.getMaxSizeUploadAvarieImage()).isEqualTo(1024L * 1024L);
        BDDAssertions.then((Object) config.getPublicAttachmentsDirectory()).isEqualTo(Paths.get("ici/la"));
    }

    @Test
    public void testOtherInternalConfigProviderRead() {
        ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
        builder.configProperty(ConfigProperty.toString("server.nomade-servlet.url", ConfigFields.nomadeSerlvetUrl, null),
                ConfigProperty.toGeneric("server.service-impl.type", ConfigFields.serviceImplType, IConfig.ServiceImplType::valueOf, IConfig.ServiceImplType.Fake),
                ConfigProperty.toLong("server.max-image-upload-avarie", ConfigFields.maxSizeUploadAvarieImage, 1024L * 1024L));
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("others/others.properties").build());
        IConfig config = builder.build();

        BDDAssertions.then(config.getNomadeSerlvetUrl()).isEqualTo("https://others.talanlabs.com");
        BDDAssertions.then(config.getServiceImplType()).isEqualTo(IConfig.ServiceImplType.NomadeServlet);
        BDDAssertions.then(config.getMaxSizeUploadAvarieImage()).isEqualTo(1024L * 1024L);
    }

    @Test
    public void testExternalConfigProviderRead() throws IOException {
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("test", ".properties");
            Files.write(tempFile, Arrays.asList("server.nomade-servlet.url=https://external.talanlabs.com", "server.service-impl.type=RusService", "server.max-image-upload-avarie=1024"), StandardOpenOption.WRITE);

            System.getProperties().put("config.file", tempFile.toFile().getAbsolutePath());

            ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
            builder.configProperty(ConfigProperty.toString("server.nomade-servlet.url", ConfigFields.nomadeSerlvetUrl, null),
                    ConfigProperty.toGeneric("server.service-impl.type", ConfigFields.serviceImplType, IConfig.ServiceImplType::valueOf, IConfig.ServiceImplType.Fake),
                    ConfigProperty.toLong("server.max-image-upload-avarie", ConfigFields.maxSizeUploadAvarieImage, 1024L * 1024L /* 1Mo */));
            IConfig config = builder.build();

            BDDAssertions.then(config.getNomadeSerlvetUrl()).isEqualTo("https://external.talanlabs.com");
            BDDAssertions.then(config.getServiceImplType()).isEqualTo(IConfig.ServiceImplType.RusService);
            BDDAssertions.then(config.getMaxSizeUploadAvarieImage()).isEqualTo(1024L);
        } finally {
            if (tempFile != null) {
                Files.deleteIfExists(tempFile);
            }
            System.getProperties().remove("config.file");
        }
    }

    @Test
    public void testExceptionInternalConfigProviderRead() {
        ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
        builder.configProperty(ConfigProperty.toString("server.nomade-servlet.url", ConfigFields.nomadeSerlvetUrl, null),
                ConfigProperty.toGeneric("server.service-impl.type", ConfigFields.serviceImplType, IConfig.ServiceImplType::valueOf, IConfig.ServiceImplType.Fake),
                ConfigProperty.toLong("server.max-image-upload-avarie", ConfigFields.maxSizeUploadAvarieImage, 1024L * 1024L));
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("error/others.properties").build());

        Assertions.assertThat(Assertions.catchThrowable(builder::build)).isExactlyInstanceOf(LoaderReadException.class);
    }

    @Test
    public void testExceptionExternalConfigProviderRead() throws IOException {
        try {
            System.getProperties().put("config.file", "/error");

            ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
            builder.configProperty(ConfigProperty.toString("server.nomade-servlet.url", ConfigFields.nomadeSerlvetUrl, null),
                    ConfigProperty.toGeneric("server.service-impl.type", ConfigFields.serviceImplType, IConfig.ServiceImplType::valueOf, IConfig.ServiceImplType.Fake),
                    ConfigProperty.toLong("server.max-image-upload-avarie", ConfigFields.maxSizeUploadAvarieImage, 1024L * 1024L /* 1Mo */));

            Assertions.assertThat(Assertions.catchThrowable(builder::build)).isExactlyInstanceOf(LoaderReadException.class);
        } finally {
            System.getProperties().remove("config.file");
        }
    }
}
