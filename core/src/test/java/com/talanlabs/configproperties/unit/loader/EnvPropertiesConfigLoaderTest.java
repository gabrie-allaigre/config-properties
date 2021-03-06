package com.talanlabs.configproperties.unit.loader;

import com.talanlabs.configproperties.loader.EnvPropertiesConfigLoader;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;

public class EnvPropertiesConfigLoaderTest {

    @Test
    public void testNotNull() throws IOException {
        EnvPropertiesConfigLoader envPropertiesConfigLoader = new EnvPropertiesConfigLoader();
        Assertions.assertThat(envPropertiesConfigLoader.readProperties()).isNotNull();
    }
}
