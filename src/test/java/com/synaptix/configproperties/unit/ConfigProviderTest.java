package com.synaptix.configproperties.unit;

import com.synaptix.configproperties.ConfigProperty;
import com.synaptix.configproperties.ConfigProvider;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class ConfigProviderTest {

    @Test
    public void testConfigProviderRead() {
        ConfigProvider.Builder<IConfig> builder = ConfigProvider.newBuilder(IConfig.class);
        builder.configProperty(ConfigProperty.toString("server.nomade-servlet.url", ConfigFields.nomadeSerlvetUrl, null),
                ConfigProperty.toGeneric("server.service-impl.type", ConfigFields.serviceImplType, IConfig.ServiceImplType::valueOf, IConfig.ServiceImplType.Fake),
                ConfigProperty.toLong("server.max-image-upload-avarie", ConfigFields.maxSizeUploadAvarieImage, 1024L * 1024L /* 1Mo */),
                ConfigProperty.toGeneric("server.public-attachments-path", ConfigFields.publicAttachmentsDirectory, Paths::get, Paths.get("public/attachments/")));
        IConfig config = builder.build();

        BDDAssertions.then(config.getNomadeSerlvetUrl()).isEqualTo("https://nomade.talanlabs.com");
        BDDAssertions.then(config.getServiceImplType()).isEqualTo(IConfig.ServiceImplType.Fake);
        BDDAssertions.then(config.getMaxSizeUploadAvarieImage()).isEqualTo(1024L * 1024L);
        BDDAssertions.then((Object)config.getPublicAttachmentsDirectory()).isEqualTo(Paths.get("ici/la"));
    }

    @Test
    public void testOtherInternalConfigProviderRead() {
        ConfigProvider.Builder<IConfig> builder = ConfigProvider.newBuilder(IConfig.class);
        builder.configProperty(ConfigProperty.toString("server.nomade-servlet.url", ConfigFields.nomadeSerlvetUrl, null),
                ConfigProperty.toGeneric("server.service-impl.type", ConfigFields.serviceImplType, IConfig.ServiceImplType::valueOf, IConfig.ServiceImplType.Fake),
                ConfigProperty.toLong("server.max-image-upload-avarie", ConfigFields.maxSizeUploadAvarieImage, 1024L * 1024L));
        builder.internalPropertiesPath("others/others.properties");
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

            ConfigProvider.Builder<IConfig> builder = ConfigProvider.newBuilder(IConfig.class);
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

                System.getProperties().remove("config.file");
            }
        }

    }
}
