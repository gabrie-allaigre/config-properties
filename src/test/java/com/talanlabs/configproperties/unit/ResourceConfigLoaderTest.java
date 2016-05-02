package com.talanlabs.configproperties.unit;

import com.talanlabs.configproperties.loader.ResourceConfigLoader;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;

public class ResourceConfigLoaderTest {

    @Test
    public void testDefaultResourceConfigLoader() throws IOException {
        Assertions.assertThat(new ResourceConfigLoader("config.properties").readProperties()).isNotNull();
    }

    @Test
    public void testNullResourceConfigLoader() throws IOException {
        Assertions.assertThat(Assertions.catchThrowable(() -> new ResourceConfigLoader("null.properties").readProperties())).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
