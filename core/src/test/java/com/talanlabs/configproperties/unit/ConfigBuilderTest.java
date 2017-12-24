package com.talanlabs.configproperties.unit;

import com.talanlabs.configproperties.loader.DefaultConfigLoader;
import com.talanlabs.configproperties.properties.*;
import com.talanlabs.configproperties.unit.properties.ConfigBean;
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
        com.talanlabs.configproperties.ConfigBuilder<ConfigBean> builder = com.talanlabs.configproperties.ConfigBuilder.newBuilder(ConfigBean.class);
        builder.configProperty(ConfigProperty.toString("server.nomade-servlet.url", "nomadeSerlvetUrl", null),
                ConfigProperty.toGeneric("server.service-impl.type", "serviceImplType", ConfigBean.ServiceImplType::valueOf, ConfigBean.ServiceImplType.Fake),
                ConfigProperty.toLong("server.max-image-upload-avarie", "maxSizeUploadAvarieImage", 1024L * 1024L /* 1Mo */),
                ConfigProperty.toGeneric("server.public-attachments-path", "publicAttachmentsDirectory", Paths::get, Paths.get("public/attachments/")),
                ConfigProperty.toInteger("server.number", "number", 5), ConfigProperty.toString("server.mail.smtp.host", "mailConfig.smptHost", null),
                ConfigProperty.toInteger("server.mail.smtp.port", "mailConfig.smptPort", null));
        ConfigBean config = builder.build();

        BDDAssertions.then(config.getNomadeSerlvetUrl()).isEqualTo("https://nomade.talanlabs.com");
        BDDAssertions.then(config.getServiceImplType()).isEqualTo(ConfigBean.ServiceImplType.Fake);
        BDDAssertions.then(config.getMaxSizeUploadAvarieImage()).isEqualTo(1024L * 1024L);
        BDDAssertions.then((Object) config.getPublicAttachmentsDirectory()).isEqualTo(Paths.get("ici/la"));
        BDDAssertions.then(config.getNumber()).isEqualTo(10);
        BDDAssertions.then(config.getMailConfig()).isNotNull();
        BDDAssertions.then(config.getMailConfig().getSmptHost()).isEqualTo("smpt.gmail.com");
        BDDAssertions.then(config.getMailConfig().getSmptPort()).isNull();
    }

    @Test
    public void testCollectionConfigProviderRead() {
        com.talanlabs.configproperties.ConfigBuilder<ConfigBean> builder = com.talanlabs.configproperties.ConfigBuilder.newBuilder(ConfigBean.class);
        builder.configProperty(CollectionConfigProperty.toList("server.groups", "groups", ConfigProperty.STRING_FROM_STRING, null),
                CollectionConfigProperty.toSet("server.enums", "enums", ConfigProperty.STRING_FROM_STRING, null));
        ConfigBean config = builder.build();

        BDDAssertions.then(config.getGroups()).containsExactly("admin", "user");
        BDDAssertions.then(config.getEnums()).containsExactlyInAnyOrder("FIRST", "SECOND");
    }

    @Test
    public void testArrayConfigProviderRead() {
        com.talanlabs.configproperties.ConfigBuilder<ConfigBean> builder = com.talanlabs.configproperties.ConfigBuilder.newBuilder(ConfigBean.class);
        builder.configProperty(ArrayConfigProperty.toArrayString("server.roles", "roles", null));
        ConfigBean config = builder.build();

        BDDAssertions.then(config.getRoles()).containsExactly("admin", "user");
    }

    @Test
    public void testPropertiesConfigProviderRead() {
        com.talanlabs.configproperties.ConfigBuilder<ConfigBean> builder = com.talanlabs.configproperties.ConfigBuilder.newBuilder(ConfigBean.class);
        builder.configProperty(new PropertiesConfigProperty("properties"));
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("properties/propertiesconfig.properties").build());
        ConfigBean config = builder.build();

        BDDAssertions.then(config.getProperties()).isNotNull();
        BDDAssertions.then(config.getProperties().size()).isEqualTo(2);
    }

    @Test
    public void testMapConfigProviderRead() {
        com.talanlabs.configproperties.ConfigBuilder<ConfigBean> builder = com.talanlabs.configproperties.ConfigBuilder.newBuilder(ConfigBean.class);
        builder.configProperty(MapConfigProperty.toHashMap("server.groups", "groupMap", ConfigProperty.STRING_FROM_STRING, ConfigProperty.STRING_FROM_STRING, null));
        builder.configProperty(MapConfigProperty.toHashMap("server.types", "typeMap", ConfigProperty.INTEGER_FROM_STRING, ConfigBean.ServiceImplType::valueOf, null));
        builder.configProperty(MapConfigProperty.toHashMap("server.booleans", "booleanMap", ConfigBean.ServiceImplType::valueOf, ConfigProperty.BOOLEAN_FROM_STRING, null));
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("properties/mapconfig.properties").build());
        ConfigBean config = builder.build();

        BDDAssertions.then(config.getGroupMap()).containsEntry("ADMIN", "gaby").containsEntry("USER", "sandra");
        BDDAssertions.then(config.getTypeMap()).containsEntry(1, ConfigBean.ServiceImplType.NomadeServlet).containsEntry(2, ConfigBean.ServiceImplType.Fake);
        BDDAssertions.then(config.getBooleanMap()).containsEntry(ConfigBean.ServiceImplType.NomadeServlet, true).containsEntry(ConfigBean.ServiceImplType.Fake, false);
    }

    @Test
    public void testOtherInternalConfigProviderRead() {
        com.talanlabs.configproperties.ConfigBuilder<ConfigBean> builder = com.talanlabs.configproperties.ConfigBuilder.newBuilder(ConfigBean.class);
        builder.configProperty(ConfigProperty.toString("server.nomade-servlet.url", "nomadeSerlvetUrl", null),
                ConfigProperty.toGeneric("server.service-impl.type", "serviceImplType", ConfigBean.ServiceImplType::valueOf, ConfigBean.ServiceImplType.Fake),
                ConfigProperty.toLong("server.max-image-upload-avarie", "maxSizeUploadAvarieImage", 1024L * 1024L));
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("others/others.properties").build());
        ConfigBean config = builder.build();

        BDDAssertions.then(config.getNomadeSerlvetUrl()).isEqualTo("https://others.talanlabs.com");
        BDDAssertions.then(config.getServiceImplType()).isEqualTo(ConfigBean.ServiceImplType.NomadeServlet);
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

            com.talanlabs.configproperties.ConfigBuilder<ConfigBean> builder = com.talanlabs.configproperties.ConfigBuilder.newBuilder(ConfigBean.class);
            builder.configProperty(ConfigProperty.toString("server.nomade-servlet.url", "nomadeSerlvetUrl", null),
                    ConfigProperty.toGeneric("server.service-impl.type", "serviceImplType", ConfigBean.ServiceImplType::valueOf, ConfigBean.ServiceImplType.Fake),
                    ConfigProperty.toLong("server.max-image-upload-avarie", "maxSizeUploadAvarieImage", 1024L * 1024L /* 1Mo */));
            builder.configLoader(DefaultConfigLoader.newBuilder().systemPropertyName("config").build());
            ConfigBean config = builder.build();

            BDDAssertions.then(config.getNomadeSerlvetUrl()).isEqualTo("https://external.talanlabs.com");
            BDDAssertions.then(config.getServiceImplType()).isEqualTo(ConfigBean.ServiceImplType.RusService);
            BDDAssertions.then(config.getMaxSizeUploadAvarieImage()).isEqualTo(1024L);
        } finally {
            if (tempFile != null) {
                Files.deleteIfExists(tempFile);
            }
            System.getProperties().remove("config");
        }
    }

    @Test
    public void testCollection2ConfigProviderRead() {
        com.talanlabs.configproperties.ConfigBuilder<ConfigBean> builder = com.talanlabs.configproperties.ConfigBuilder.newBuilder(ConfigBean.class);
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("properties/col-config.properties").build());
        builder.configProperty(CollectionMultiLinesConfigProperty.toList("server.groups", "groups", ConfigProperty.STRING_FROM_STRING, null),
                CollectionMultiLinesConfigProperty.toSet("server.enums", "enums", ConfigProperty.STRING_FROM_STRING, null));
        ConfigBean config = builder.build();

        BDDAssertions.then(config.getGroups()).containsExactly("admin", "user");
        BDDAssertions.then(config.getEnums()).containsExactlyInAnyOrder("FIRST", "SECOND");
    }

    @Test
    public void testArray2ConfigProviderRead() {
        com.talanlabs.configproperties.ConfigBuilder<ConfigBean> builder = com.talanlabs.configproperties.ConfigBuilder.newBuilder(ConfigBean.class);
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("properties/array-config.properties").build());
        builder.configProperty(ArrayMultiLinesConfigProperty.toArrayString("server.roles", "roles", null));
        ConfigBean config = builder.build();

        BDDAssertions.then(config.getRoles()).containsExactly("admin", "user");
    }
}
