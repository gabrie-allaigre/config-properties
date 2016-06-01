package com.synaptix.configproperties.unit;

import com.synaptix.configproperties.loader.EnvPropertiesConfigLoader;
import com.synaptix.configproperties.loader.SystemPropertiesConfigLoader;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;

public class EnvPropertiesConfigLoaderTest {

    @Test
    public void testDefaultSystemPropertiesConfigLoader() throws IOException {
        EnvPropertiesConfigLoader envPropertiesConfigLoader = new EnvPropertiesConfigLoader();
        Assertions.assertThat(envPropertiesConfigLoader.readProperties()).isNotNull();
    }
}
