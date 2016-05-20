package com.synaptix.configproperties.unit;

import com.synaptix.configproperties.loader.SystemConfigLoader;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class SystemConfigLoaderTest {

    @Test
    public void testDefaultSystemConfigLoader() throws IOException {
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("test", ".properties");
            Files.write(tempFile, Arrays.asList("server.nomade-servlet.url=https://external.talanlabs.com", "server.service-impl.type=RusService", "server.max-image-upload-avarie=1024"),
                    StandardOpenOption.WRITE);

            System.getProperties().put("config.file", tempFile.toFile().getAbsolutePath());

            SystemConfigLoader systemConfigLoader = new SystemConfigLoader();
            Assertions.assertThat(systemConfigLoader.readProperties()).isNotNull();
        } finally {
            if (tempFile != null) {
                Files.deleteIfExists(tempFile);
            }
            System.getProperties().remove("config.file");
        }
    }

    @Test
    public void testNullSystemConfigLoader() {
        SystemConfigLoader systemConfigLoader = new SystemConfigLoader();
        Assertions.assertThat(systemConfigLoader.readProperties()).isNull();
    }

    @Test
    public void testOtherSystemConfigLoader() throws IOException {
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("test", ".properties");
            Files.write(tempFile, Arrays.asList("server.nomade-servlet.url=https://external.talanlabs.com", "server.service-impl.type=RusService", "server.max-image-upload-avarie=1024"),
                    StandardOpenOption.WRITE);

            System.getProperties().put("other.file", tempFile.toFile().getAbsolutePath());

            SystemConfigLoader systemConfigLoader = new SystemConfigLoader("other.file");
            Assertions.assertThat(systemConfigLoader.readProperties()).isNotNull();
        } finally {
            if (tempFile != null) {
                Files.deleteIfExists(tempFile);
            }
            System.getProperties().remove("other.file");
        }
    }
}
