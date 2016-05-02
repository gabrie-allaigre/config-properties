package com.talanlabs.configproperties.loader;

import java.util.Properties;

public class SystemPropertiesConfigLoader implements IConfigLoader {

    public SystemPropertiesConfigLoader() {
        super();
    }

    @Override
    public Properties readProperties() {
        return System.getProperties();
    }
}
