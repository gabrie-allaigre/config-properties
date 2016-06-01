package com.synaptix.configproperties.loader;

import java.io.File;
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
