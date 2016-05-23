package com.synaptix.configproperties.unit;

import com.synaptix.configproperties.loader.ComposeConfigLoader;
import com.synaptix.configproperties.loader.IConfigLoader;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Properties;

public class ComposeConfigLoaderTest {

    @Test
    public void testDefaultComposeConfigLoader() throws IOException {
        Properties properties = new ComposeConfigLoader(IConfigLoader.system("config.file"), IConfigLoader.resource("config.properties")).readProperties();
        Assertions.assertThat(properties).isNotNull();
        Assertions.assertThat(properties.getProperty("server.service-impl.type")).isEqualTo("Fake");
    }

    @Test
    public void testFullComposeConfigLoader() throws IOException {
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("test", ".properties");
            Files.write(tempFile,
                    Arrays.asList("server.nomade-servlet.url=https://external.talanlabs.com", "server.service-impl.type=RusService", "server.max-image-upload-avarie=1024", "unique=true"),
                    StandardOpenOption.WRITE);

            System.getProperties().put("config.file", tempFile.toFile().getAbsolutePath());

            Properties properties = IConfigLoader.compose(IConfigLoader.system("config.file"), IConfigLoader.resource("config.properties")).readProperties();
            Assertions.assertThat(properties).isNotNull();
            Assertions.assertThat(properties.getProperty("server.service-impl.type")).isEqualTo("Fake");
            Assertions.assertThat(properties.getProperty("server.mail.smtp.host")).isEqualTo("smpt.gmail.com");
            Assertions.assertThat(properties.getProperty("unique")).isEqualTo("true");
        } finally {
            if (tempFile != null) {
                Files.deleteIfExists(tempFile);
            }
            System.getProperties().remove("config.file");
        }
    }

    @Test
    public void testNullComposeConfigLoader() throws IOException {
        Assertions.assertThat(new ComposeConfigLoader(IConfigLoader.system("config.file"), IConfigLoader.system("config2.file")).readProperties()).isNull();
    }
}
