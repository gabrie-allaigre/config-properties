package com.talanlabs.configproperties.loader;

import java.util.Properties;

public class PropertiesConfigLoader implements IConfigLoader {

    private final Properties properties;

    public PropertiesConfigLoader(Properties properties) {
        super();

        this.properties = properties;
    }

    @Override
    public Properties readProperties() {
        return properties;
    }
}
