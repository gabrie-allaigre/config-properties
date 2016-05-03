package com.synaptix.configproperties.loader;

import com.google.common.io.Resources;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

public class DefaultConfigLoader implements IConfigLoader {

    private static final Logger LOG = LogManager.getLogger(DefaultConfigLoader.class);

    public static final String DEFAULT_SYSTEM_PROPERTY_NAME = "config.file";
    public static final String DEFAULT_INTERNAL_PROPERTIES_PATH = "config.properties";

    private final String systemPropertyName;
    private final String internalPropertiesPath;

    public static class Builder {

        private String systemPropertyName;
        private String internalPropertiesPath;

        public Builder systemPropertyName(String systemPropertyName) {
            this.systemPropertyName = systemPropertyName;
            return this;
        }

        public Builder internalPropertiesPath(String internalPropertiesPath) {
            this.internalPropertiesPath = internalPropertiesPath;
            return this;
        }

        public DefaultConfigLoader build() {
            if (systemPropertyName == null) {
                systemPropertyName = DEFAULT_SYSTEM_PROPERTY_NAME;
            }
            if (internalPropertiesPath == null) {
                internalPropertiesPath = DEFAULT_INTERNAL_PROPERTIES_PATH;
            }
            return new DefaultConfigLoader(systemPropertyName, internalPropertiesPath);
        }

    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public DefaultConfigLoader(String systemPropertyName, String internalPropertiesPath) {
        super();

        this.systemPropertyName = systemPropertyName;
        this.internalPropertiesPath = internalPropertiesPath;
    }

    @Override
    public Properties readProperties() {
        Properties properties = new Properties();

        String configFilePath = System.getProperty(systemPropertyName);
        if (!StringUtils.isBlank(configFilePath)) {
            LOG.info("Read config file in external " + configFilePath);
            try (Reader reader = new FileReader(configFilePath)) {
                properties.load(reader);
            } catch (IOException e) {
                LOG.error("Not read external file " + configFilePath, e);
                throw new LoaderReadError("Not read external file " + configFilePath, e);
            }
        } else {
            LOG.info("Read config file in internal");
            try (InputStream in = Resources.asByteSource(Resources.getResource(internalPropertiesPath)).openStream()) {
                properties.load(in);
            } catch (IOException e) {
                LOG.error("Not read internal file " + internalPropertiesPath, e);
                throw new LoaderReadError("Not read internal file " + internalPropertiesPath, e);
            }
        }
        return properties;
    }

}
