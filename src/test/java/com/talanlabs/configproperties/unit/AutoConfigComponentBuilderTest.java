package com.talanlabs.configproperties.unit;

import com.talanlabs.configproperties.ComponentConfigBuilder;
import com.talanlabs.configproperties.ConfigBuilder;
import com.talanlabs.configproperties.loader.DefaultConfigLoader;
import com.talanlabs.configproperties.properties.AutoConfigProperty;
import com.talanlabs.configproperties.unit.properties.IAutoConfig;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;

public class AutoConfigComponentBuilderTest {

    @Test
    public void testAutoConfigProperties() {
        ComponentConfigBuilder<IAutoConfig> builder = ComponentConfigBuilder.newBuilder(IAutoConfig.class);
        builder.configProperty(AutoConfigProperty.newBuilder().build());
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("properties/autoconfig1.properties").build());
        IAutoConfig config = builder.build();

        BDDAssertions.then(config.getName()).isEqualTo("Toto");
        BDDAssertions.then(config.getType()).isEqualTo(IAutoConfig.Type.NomadeServlet);
        BDDAssertions.then(config.getSize()).isEqualTo(10);
        BDDAssertions.then(config.getWidth()).isEqualTo(51);
        BDDAssertions.then(config.getTiti()).isEqualTo(15);
        BDDAssertions.then(config.getSubConfig().getSmtpHost()).isEqualTo("rien.com");
        BDDAssertions.then(config.getSubConfig().getSmtpPort()).isEqualTo(22);
        BDDAssertions.then(config.getSubConfig().getAuth().getLogin()).isEqualTo("admin");
        BDDAssertions.then(config.getSubConfig().getAuth().getPassword()).isEqualTo("demo");
    }

    @Test
    public void testAutoConfigYaml() {
        ComponentConfigBuilder<IAutoConfig> builder = ComponentConfigBuilder.newBuilder(IAutoConfig.class);
        builder.configProperty(AutoConfigProperty.newBuilder().build());
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("yaml/autoconfig1.yml").build());
        IAutoConfig config = builder.build();

        BDDAssertions.then(config.getName()).isEqualTo("Toto");
        BDDAssertions.then(config.getType()).isEqualTo(IAutoConfig.Type.NomadeServlet);
        BDDAssertions.then(config.getSize()).isEqualTo(10);
        BDDAssertions.then(config.getWidth()).isEqualTo(51);
        BDDAssertions.then(config.getTiti()).isEqualTo(15);
        BDDAssertions.then(config.getSubConfig().getSmtpHost()).isEqualTo("rien.com");
        BDDAssertions.then(config.getSubConfig().getSmtpPort()).isEqualTo(22);
        BDDAssertions.then(config.getSubConfig().getAuth().getLogin()).isEqualTo("admin");
        BDDAssertions.then(config.getSubConfig().getAuth().getPassword()).isEqualTo("demo");
    }

    @Test
    public void testAutoConfigPrefixProperties() {
        ComponentConfigBuilder<IAutoConfig> builder = ComponentConfigBuilder.newBuilder(IAutoConfig.class);
        builder.configProperty(AutoConfigProperty.newBuilder().prefix("server").build());
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("properties/autoconfig2.properties").build());
        IAutoConfig config = builder.build();

        BDDAssertions.then(config.getName()).isEqualTo("Toto");
        BDDAssertions.then(config.getType()).isEqualTo(IAutoConfig.Type.NomadeServlet);
        BDDAssertions.then(config.getSize()).isEqualTo(10);
        BDDAssertions.then(config.getWidth()).isEqualTo(25);
        BDDAssertions.then(config.getSubConfig().getSmtpHost()).isEqualTo("rien.com");
        BDDAssertions.then(config.getSubConfig().getSmtpPort()).isEqualTo(22);
        BDDAssertions.then(config.getRoles()).containsExactly("admin", "user");
        BDDAssertions.then(config.getGroups()).containsExactly("admin", "user");
        BDDAssertions.then(config.getEnums()).containsExactlyInAnyOrder(IAutoConfig.Type.RusService, IAutoConfig.Type.Fake);
        BDDAssertions.then(config.getGroupMap()).containsEntry("ADMIN", "gaby").containsEntry("USER", "sandra");
        BDDAssertions.then(config.getTypeMap()).containsEntry(1, IAutoConfig.Type.NomadeServlet).containsEntry(2, IAutoConfig.Type.Fake);
        BDDAssertions.then(config.getBooleanMap()).containsEntry(IAutoConfig.Type.NomadeServlet, true).containsEntry(IAutoConfig.Type.Fake, false);
    }

    @Test
    public void testAutoConfigPrefixYaml() {
        ComponentConfigBuilder<IAutoConfig> builder = ComponentConfigBuilder.newBuilder(IAutoConfig.class);
        builder.configProperty(AutoConfigProperty.newBuilder().prefix("server").build());
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("yaml/autoconfig2.yml").build());
        IAutoConfig config = builder.build();

        BDDAssertions.then(config.getName()).isEqualTo("Toto");
        BDDAssertions.then(config.getType()).isEqualTo(IAutoConfig.Type.NomadeServlet);
        BDDAssertions.then(config.getSize()).isEqualTo(10);
        BDDAssertions.then(config.getWidth()).isEqualTo(25);
        BDDAssertions.then(config.getSubConfig().getSmtpHost()).isEqualTo("rien.com");
        BDDAssertions.then(config.getSubConfig().getSmtpPort()).isEqualTo(22);
        BDDAssertions.then(config.getProperties()).isNotNull();
    }

    @Test
    public void testAutoConfigAltProperties() {
        ComponentConfigBuilder<IAutoConfig> builder = ComponentConfigBuilder.newBuilder(IAutoConfig.class);
        builder.configProperty(AutoConfigProperty.newBuilder().prefix("server").build());
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("properties/autoconfig4.properties").build());
        IAutoConfig config = builder.build();

        BDDAssertions.then(config.getSubConfig().getSmtpHost()).isEqualTo("google.com");
        BDDAssertions.then(config.getSubConfig().getAuth().getLogin()).isEqualTo("gaby");
        BDDAssertions.then(config.getSubConfig().getAuth().getPassword()).isEqualTo("toto");
        BDDAssertions.then(config.getSubConfig().getAuth().isSsl()).isFalse();
        BDDAssertions.then(config.getSubConfig().getAuth().isAuto()).isFalse();
    }

    @Test
    public void testAutoConfigListProperties() {
        ComponentConfigBuilder<IAutoConfig> builder = ComponentConfigBuilder.newBuilder(IAutoConfig.class);
        builder.configProperty(AutoConfigProperty.newBuilder().prefix("server").build());
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("properties/autoconfig5.properties").build());
        IAutoConfig config = builder.build();

        BDDAssertions.then(config.getRoles()).containsExactly("admin", "user");
        BDDAssertions.then(config.getGroups()).containsExactly("admin", "user");
        BDDAssertions.then(config.getEnums()).containsExactlyInAnyOrder(IAutoConfig.Type.RusService, IAutoConfig.Type.Fake);
    }

    @Test
    public void testAutoConfigMapProperties() {
        ComponentConfigBuilder<IAutoConfig> builder = ComponentConfigBuilder.newBuilder(IAutoConfig.class);
        builder.configProperty(AutoConfigProperty.newBuilder().prefix("server").build());
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("properties/autoconfig6.properties").build());
        IAutoConfig config = builder.build();

        BDDAssertions.then(config.getGroupMap()).containsEntry("ADMIN", "gaby").containsEntry("USER", "sandra");
        BDDAssertions.then(config.getTypeMap()).containsEntry(1, IAutoConfig.Type.NomadeServlet).containsEntry(2, IAutoConfig.Type.Fake);
        BDDAssertions.then(config.getBooleanMap()).containsEntry(IAutoConfig.Type.NomadeServlet, true).containsEntry(IAutoConfig.Type.Fake, false);
    }

    @Test
    public void testAutoConfigMapAltProperties() {
        ComponentConfigBuilder<IAutoConfig> builder = ComponentConfigBuilder.newBuilder(IAutoConfig.class);
        builder.configProperty(AutoConfigProperty.newBuilder().prefix("server").build());
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("properties/autoconfig7.properties").build());
        IAutoConfig config = builder.build();

        BDDAssertions.then(config.getGroupMap()).containsEntry("ADMIN", "laureline").containsEntry("USER", "sandra").containsEntry("SIMPLE", "raphael").hasSize(3);
        BDDAssertions.then(config.getGroupMap2()).containsEntry("ADMIN", "laureline").containsEntry("SIMPLE", "raphael").hasSize(2);
    }

    @Test
    public void testAutoConfigListYaml() {
        ComponentConfigBuilder<IAutoConfig> builder = ComponentConfigBuilder.newBuilder(IAutoConfig.class);
        builder.configProperty(AutoConfigProperty.newBuilder().prefix("server").build());
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("yaml/autoconfig3.yml").build());
        IAutoConfig config = builder.build();

        BDDAssertions.then(config.getRoles()).containsExactly("admin1", "user2", "admin3", "user4", "admin5", "user6", "admin7",
                "user8", "admin9", "user10");
        BDDAssertions.then(config.getGroups()).containsExactly("admin", "user");
        BDDAssertions.then(config.getEnums()).containsExactlyInAnyOrder(IAutoConfig.Type.RusService, IAutoConfig.Type.Fake);
    }

    @Test
    public void testAutoConfigMapYaml() {
        ComponentConfigBuilder<IAutoConfig> builder = ComponentConfigBuilder.newBuilder(IAutoConfig.class);
        builder.configProperty(AutoConfigProperty.newBuilder().prefix("server").build());
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("yaml/autoconfig4.yml").build());
        IAutoConfig config = builder.build();

        BDDAssertions.then(config.getGroupMap()).containsEntry("ADMIN", "gaby").containsEntry("USER", "sandra");
        BDDAssertions.then(config.getTypeMap()).containsEntry(1, IAutoConfig.Type.NomadeServlet).containsEntry(2, IAutoConfig.Type.Fake);
        BDDAssertions.then(config.getBooleanMap()).containsEntry(IAutoConfig.Type.NomadeServlet, true).containsEntry(IAutoConfig.Type.Fake, false);
    }
}
