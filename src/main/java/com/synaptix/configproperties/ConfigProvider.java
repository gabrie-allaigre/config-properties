package com.synaptix.configproperties;

import com.google.common.io.Resources;
import com.synaptix.component.IComponent;
import com.synaptix.component.factory.ComponentFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigProvider<E extends IComponent> {

    private static final Logger LOG = LogManager.getLogger(ConfigProvider.class);

    private final Class<E> componentClass;
    private final String systemPropertyName;
    private final String internalPropertiesPath;
    private final List<ConfigProperty<?>> configProperties;

    public static <E extends IComponent> Builder<E> newBuilder(Class<E> componentClass) {
        return new Builder<>(componentClass);
    }

    private ConfigProvider(Class<E> componentClass, List<ConfigProperty<?>> configProperties, String systemPropertyName, String internalPropertiesPath) {
        super();

        this.componentClass = componentClass;
        this.systemPropertyName = systemPropertyName;
        this.internalPropertiesPath = internalPropertiesPath;
        this.configProperties = configProperties;
    }

    private E create() {
        Properties properties = new Properties();

        String configFilePath = System.getProperty(systemPropertyName);
        if (!StringUtils.isBlank(configFilePath)) {
            LOG.info("Read config file in external " + configFilePath);
            try (Reader reader = new FileReader(configFilePath)) {
                properties.load(reader);
            } catch (IOException e) {
                LOG.error("Not read external file " + configFilePath, e);
                throw new RuntimeException("Not read external file " + configFilePath, e);
            }
        } else {
            LOG.info("Read config file in internal");
            try (InputStream in = Resources.asByteSource(Resources.getResource(internalPropertiesPath)).openStream()) {
                properties.load(in);
            } catch (IOException e) {
                LOG.error("Not read internal file " + internalPropertiesPath, e);
                throw new RuntimeException("Not read internal file " + internalPropertiesPath, e);
            }
        }

        E config = ComponentFactory.getInstance().createInstance(componentClass);
        configProperties.forEach(configProperty -> setProperty(configProperty, properties, config));
        return config;
    }

    private void setProperty(ConfigProperty<?> configProperty, Properties properties, E config) {
        Object res = configProperty.setProperty(properties, config);
        LOG.info(String.format("Config property %1s = %2s", configProperty.getKey(), res));
    }

    public static class Builder<E extends IComponent> {

        private final Class<E> componentClass;
        private final List<ConfigProperty<?>> configProperties;

        private String systemPropertyName = "config.file";
        private String internalPropertiesPath = "config.properties";

        public Builder(Class<E> componentClass) {
            super();
            this.componentClass = componentClass;

            this.configProperties = new ArrayList<>();
        }

        public Builder systemPropertyName(String systemPropertyName) {
            this.systemPropertyName = systemPropertyName;
            return this;
        }

        public Builder internalPropertiesPath(String internalPropertiesPath) {
            this.internalPropertiesPath = internalPropertiesPath;
            return this;
        }

        public Builder configProperty(ConfigProperty<?> configProperty, ConfigProperty<?>... configProperties) {
            this.configProperties.add(configProperty);
            if (configProperties != null) {
                this.configProperties.addAll(Arrays.asList(configProperties));
            }
            return this;
        }

        public E build() {
            return new ConfigProvider<>(componentClass, configProperties, systemPropertyName, internalPropertiesPath).create();
        }
    }
}
