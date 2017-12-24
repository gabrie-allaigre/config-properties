package com.talanlabs.configproperties.unit.loader;

import com.talanlabs.configproperties.loader.SystemPropertiesConfigLoader;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;

public class SystemPropertiesConfigLoaderTest {

    @Test
    public void testDefaultSystemPropertiesConfigLoader() throws IOException {
        try {
            System.getProperties().put("server.mail.to", "test@test.com");

            SystemPropertiesConfigLoader systemPropertiesConfigLoader = new SystemPropertiesConfigLoader();
            Assertions.assertThat(systemPropertiesConfigLoader.readProperties()).isNotNull().containsEntry("server.mail.to", "test@test.com");
        } finally {
            System.getProperties().remove("server.mail.to");
        }
    }
}
