package com.synaptix.configproperties.unit;

import com.synaptix.configproperties.ConfigBuilder;
import com.synaptix.configproperties.loader.DefaultConfigLoader;
import com.synaptix.configproperties.loader.LoaderReadException;
import com.synaptix.configproperties.properties.*;
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
                ConfigProperty.toGeneric("server.public-attachments-path", ConfigFields.publicAttachmentsDirectory, Paths::get, Paths.get("public/attachments/")),
                ConfigProperty.toInteger("server.number", ConfigFields.number, 5), ConfigProperty.toString("server.mail.smtp.host", ConfigFields.mailConfig().dot().smptHost().name(), null),
                ConfigProperty.toInteger("server.mail.smtp.port", ConfigFields.mailConfig().dot().smptPort().name(), null));
        IConfig config = builder.build();

        BDDAssertions.then(config.getNomadeSerlvetUrl()).isEqualTo("https://nomade.talanlabs.com");
        BDDAssertions.then(config.getServiceImplType()).isEqualTo(IConfig.ServiceImplType.Fake);
        BDDAssertions.then(config.getMaxSizeUploadAvarieImage()).isEqualTo(1024L * 1024L);
        BDDAssertions.then((Object) config.getPublicAttachmentsDirectory()).isEqualTo(Paths.get("ici/la"));
        BDDAssertions.then(config.getNumber()).isEqualTo(10);
        BDDAssertions.then(config.getMailConfig()).isNotNull();
        BDDAssertions.then(config.getMailConfig().getSmptHost()).isEqualTo("smpt.gmail.com");
        BDDAssertions.then(config.getMailConfig().getSmptPort()).isNull();
    }

    @Test
    public void testCollectionConfigProviderRead() {
        ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
        builder.configProperty(CollectionConfigProperty.toList("server.groups", ConfigFields.groups, ConfigProperty.STRING_FROM_STRING, null),
                CollectionConfigProperty.toSet("server.enums", ConfigFields.enums, ConfigProperty.STRING_FROM_STRING, null));
        IConfig config = builder.build();

        BDDAssertions.then(config.getGroups()).containsExactly("admin", "user");
        BDDAssertions.then(config.getEnums()).containsExactlyInAnyOrder("FIRST", "SECOND");
    }

    @Test
    public void testArrayConfigProviderRead() {
        ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
        builder.configProperty(ArrayConfigProperty.toArrayString("server.roles", ConfigFields.roles, null));
        IConfig config = builder.build();

        BDDAssertions.then(config.getRoles()).containsExactly("admin", "user");
    }

    @Test
    public void testPropertiesConfigProviderRead() {
        ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
        builder.configProperty(new PropertiesConfigProperty(ConfigFields.properties));
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("propertiesconfig.properties").build());
        IConfig config = builder.build();

        BDDAssertions.then(config.getProperties()).isNotNull();
        BDDAssertions.then(config.getProperties().size()).isEqualTo(2);
    }

    @Test
    public void testMapConfigProviderRead() {
        ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
        builder.configProperty(MapConfigProperty.toHashMap("server.groups", ConfigFields.groupMap, ConfigProperty.STRING_FROM_STRING, ConfigProperty.STRING_FROM_STRING, null));
        builder.configProperty(MapConfigProperty.toHashMap("server.types", ConfigFields.typeMap, ConfigProperty.INTEGER_FROM_STRING, IConfig.ServiceImplType::valueOf, null));
        builder.configProperty(MapConfigProperty.toHashMap("server.booleans", ConfigFields.booleanMap, IConfig.ServiceImplType::valueOf, ConfigProperty.BOOLEAN_FROM_STRING, null));
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("mapconfig.properties").build());
        IConfig config = builder.build();

        BDDAssertions.then(config.getGroupMap()).containsEntry("ADMIN", "gaby").containsEntry("USER", "sandra");
        BDDAssertions.then(config.getTypeMap()).containsEntry(1, IConfig.ServiceImplType.NomadeServlet).containsEntry(2, IConfig.ServiceImplType.Fake);
        BDDAssertions.then(config.getBooleanMap()).containsEntry(IConfig.ServiceImplType.NomadeServlet, true).containsEntry(IConfig.ServiceImplType.Fake, false);
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
            Files.write(tempFile, Arrays.asList("server.nomade-servlet.url=https://external.talanlabs.com", "server.service-impl.type=RusService", "server.max-image-upload-avarie=1024"),
                    StandardOpenOption.WRITE);

            System.getProperties().put("config", tempFile.toFile().getAbsolutePath());

            ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
            builder.configProperty(ConfigProperty.toString("server.nomade-servlet.url", ConfigFields.nomadeSerlvetUrl, null),
                    ConfigProperty.toGeneric("server.service-impl.type", ConfigFields.serviceImplType, IConfig.ServiceImplType::valueOf, IConfig.ServiceImplType.Fake),
                    ConfigProperty.toLong("server.max-image-upload-avarie", ConfigFields.maxSizeUploadAvarieImage, 1024L * 1024L /* 1Mo */));
            builder.configLoader(DefaultConfigLoader.newBuilder().systemPropertyName("config").build());
            IConfig config = builder.build();

            BDDAssertions.then(config.getNomadeSerlvetUrl()).isEqualTo("https://external.talanlabs.com");
            BDDAssertions.then(config.getServiceImplType()).isEqualTo(IConfig.ServiceImplType.RusService);
            BDDAssertions.then(config.getMaxSizeUploadAvarieImage()).isEqualTo(1024L);
        } finally {
            if (tempFile != null) {
                Files.deleteIfExists(tempFile);
            }
            System.getProperties().remove("config");
        }
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
