package com.talanlabs.configproperties.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;

public class FileConfigLoader implements IConfigLoader {

    private static final Logger LOG = LogManager.getLogger(FileConfigLoader.class);

    private final File file;

    public FileConfigLoader(File file) {
        super();

        this.file = file;
    }

    @Override
    public Properties readProperties() {
        if (file != null && file.exists()) {
            Properties properties = new Properties();
            LOG.info("Read config file in external " + file);
            try {
                try (Reader reader = new FileReader(file)) {
                    properties.load(reader);
                }
            } catch (Exception e) {
                LOG.error("Not read external file " + file, e);
                throw new LoaderReadException("Not read external file " + file, e);
            }
            return properties;
        }
        return null;
    }
}
