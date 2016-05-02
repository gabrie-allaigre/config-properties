package com.talanlabs.configproperties.unit;

import com.talanlabs.configproperties.ConfigBuilder;
import com.talanlabs.configproperties.loader.DefaultConfigLoader;
import com.talanlabs.configproperties.properties.AutoConfigProperty;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;

public class AutoConfigBuilderTest {

    @Test
    public void testAutoConfig() {
        ConfigBuilder<IAutoConfig> builder = ConfigBuilder.newBuilder(IAutoConfig.class);
        builder.configProperty(AutoConfigProperty.newBuilder().build());
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("autoconfig1.properties").build());
        IAutoConfig config = builder.build();

        BDDAssertions.then(config.getName()).isEqualTo("Toto");
        BDDAssertions.then(config.getType()).isEqualTo(IAutoConfig.Type.NomadeServlet);
        BDDAssertions.then(config.getSize()).isEqualTo(10);
        BDDAssertions.then(config.getWidth()).isEqualTo(51);
        BDDAssertions.then(config.getTiti()).isEqualTo(15);
        BDDAssertions.then(config.getSubConfig().getSmtpHost()).isEqualTo("rien.com");
        BDDAssertions.then(config.getSubConfig().getSmtpPort()).isEqualTo(22);
    }

    @Test
    public void testAutoConfigPrefix() {
        ConfigBuilder<IAutoConfig> builder = ConfigBuilder.newBuilder(IAutoConfig.class);
        builder.configProperty(AutoConfigProperty.newBuilder().prefix("server").build());
        builder.configLoader(DefaultConfigLoader.newBuilder().internalPropertiesPath("autoconfig2.properties").build());
        IAutoConfig config = builder.build();

        BDDAssertions.then(config.getName()).isEqualTo("Toto");
        BDDAssertions.then(config.getType()).isEqualTo(IAutoConfig.Type.NomadeServlet);
        BDDAssertions.then(config.getSize()).isEqualTo(10);
        BDDAssertions.then(config.getSubConfig().getSmtpHost()).isEqualTo("rien.com");
        BDDAssertions.then(config.getSubConfig().getSmtpPort()).isEqualTo(22);
    }
}
