package com.talanlabs.configproperties.loader;

import java.util.Properties;

public class DefaultConfigLoader implements IConfigLoader {

    private final IConfigLoader compositeConfigLoader;

    public DefaultConfigLoader(String systemPropertyName, String internalPropertiesPath) {
        super();

        this.compositeConfigLoader = new FirstNotNullConfigLoader(new SystemConfigLoader(systemPropertyName), new ResourceConfigLoader(internalPropertiesPath));
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public Properties readProperties() {
        return compositeConfigLoader.readProperties();
    }

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
                systemPropertyName = SystemConfigLoader.DEFAULT_SYSTEM_PROPERTY_NAME;
            }
            if (internalPropertiesPath == null) {
                internalPropertiesPath = ResourceConfigLoader.DEFAULT_INTERNAL_PROPERTIES_PATH;
            }
            return new DefaultConfigLoader(systemPropertyName, internalPropertiesPath);
        }

    }
}
