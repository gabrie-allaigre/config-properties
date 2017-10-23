package com.talanlabs.configproperties.unit.loader;

import com.talanlabs.configproperties.loader.FirstNotNullConfigLoader;
import com.talanlabs.configproperties.loader.IConfigLoader;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Properties;

public class FirstNotNullConfigLoaderTest {

    @Test
    public void testSecondFirstNotNullConfigLoader() throws IOException {
        Properties properties = new FirstNotNullConfigLoader(IConfigLoader.system("config.file"), IConfigLoader.resource("config.properties")).readProperties();
        Assertions.assertThat(properties).isNotNull();
        Assertions.assertThat(properties.getProperty("server.service-impl.type")).isEqualTo("Fake");
    }

    @Test
    public void testFirstFirstNotNullConfigLoader() throws IOException {
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("test", ".properties");
            Files.write(tempFile, Arrays.asList("server.nomade-servlet.url=https://external.talanlabs.com", "server.service-impl.type=RusService", "server.max-image-upload-avarie=1024"),
                    StandardOpenOption.WRITE);

            System.getProperties().put("config.file", tempFile.toFile().getAbsolutePath());

            Properties properties = IConfigLoader.firstNotNull(IConfigLoader.system("config.file"), IConfigLoader.resource("config.properties")).readProperties();
            Assertions.assertThat(properties).isNotNull();
            Assertions.assertThat(properties.getProperty("server.service-impl.type")).isEqualTo("RusService");
        } finally {
            if (tempFile != null) {
                Files.deleteIfExists(tempFile);
            }
            System.getProperties().remove("config.file");
        }
    }

    @Test
    public void testNullFirstNotNullConfigLoader() throws IOException {
        Assertions.assertThat(new FirstNotNullConfigLoader(IConfigLoader.system("config.file"), IConfigLoader.system("config2.file")).readProperties()).isNull();
    }
}
