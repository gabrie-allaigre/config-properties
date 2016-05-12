package com.synaptix.configproperties.loader;

import com.google.common.io.Resources;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

public class SystemConfigLoader implements IConfigLoader {

    private static final Logger LOG = LogManager.getLogger(SystemConfigLoader.class);

    public static final String DEFAULT_SYSTEM_PROPERTY_NAME = "config.file";

    private final String systemPropertyName;

    public SystemConfigLoader() {
        this(DEFAULT_SYSTEM_PROPERTY_NAME);
    }

    public SystemConfigLoader(String systemPropertyName) {
        super();

        this.systemPropertyName = systemPropertyName;
    }

    @Override
    public Properties readProperties() {
        String configFilePath = System.getProperty(systemPropertyName);
        if (!StringUtils.isBlank(configFilePath)) {
            Properties properties = new Properties();
            LOG.info("Read config file in external " + configFilePath);
            try {
                try (Reader reader = new FileReader(configFilePath)) {
                    properties.load(reader);
                }
            } catch (Exception e) {
                LOG.error("Not read external file " + configFilePath, e);
                throw new LoaderReadException("Not read external file " + configFilePath, e);
            }
            return properties;
        }
        return null;
    }
}
