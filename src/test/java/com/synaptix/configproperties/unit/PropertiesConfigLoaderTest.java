package com.synaptix.configproperties.unit;

import com.synaptix.configproperties.loader.PropertiesConfigLoader;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

public class PropertiesConfigLoaderTest {

    @Test
    public void testDefaultPropertiesConfigLoader() throws IOException {
        Properties properties = new Properties();
        properties.put("server.mail.to", "test@test.com");

        PropertiesConfigLoader propertiesConfigLoader = new PropertiesConfigLoader(properties);
        Assertions.assertThat(propertiesConfigLoader.readProperties()).isNotNull().containsEntry("server.mail.to", "test@test.com");
    }

    @Test
    public void testNullPropertiesConfigLoader() throws IOException {
        PropertiesConfigLoader propertiesConfigLoader = new PropertiesConfigLoader(null);
        Assertions.assertThat(propertiesConfigLoader.readProperties()).isNull();
    }
}
