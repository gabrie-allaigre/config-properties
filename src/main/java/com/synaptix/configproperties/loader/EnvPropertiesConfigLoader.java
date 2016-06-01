package com.synaptix.configproperties.loader;

import java.util.Properties;

public class EnvPropertiesConfigLoader implements IConfigLoader {

    public EnvPropertiesConfigLoader() {
        super();
    }

    @Override
    public Properties readProperties() {
        Properties properties = new Properties();
        properties.putAll(System.getenv());
        return properties;
    }
}
