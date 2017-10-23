package com.talanlabs.configproperties.unit.loader;

import com.google.common.io.Resources;
import com.talanlabs.configproperties.loader.LoaderReadException;
import com.talanlabs.configproperties.loader.URLConfigLoader;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class URLConfigLoaderTest {

    @Test
    public void testDefaultURLConfigLoader() throws IOException {
        Assertions.assertThat(new URLConfigLoader(Resources.getResource("config.properties")).readProperties()).isNotNull();
    }

    @Test
    public void testYamlURLConfigLoader() throws IOException {
        Assertions.assertThat(new URLConfigLoader(Resources.getResource("yaml/autoconfig1.yml")).readProperties()).isNotNull();
    }

    @Test
    public void testUnknownURLConfigLoader() throws IOException {
        Assertions.assertThat(Assertions.catchThrowable(() -> new URLConfigLoader(Resources.getResource("config.txt")).readProperties())).isExactlyInstanceOf(LoaderReadException.class);
    }

    @Test
    public void testNullURLConfigLoader() throws IOException {
        Assertions.assertThat(Assertions.catchThrowable(() -> new URLConfigLoader(new URL("http://nexistepas.com")).readProperties())).isExactlyInstanceOf(LoaderReadException.class);
    }
}
