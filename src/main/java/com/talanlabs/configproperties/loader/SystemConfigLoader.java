package com.talanlabs.configproperties.loader;

import java.io.File;

public class SystemConfigLoader extends FileConfigLoader {

    public static final String DEFAULT_SYSTEM_PROPERTY_NAME = "config.file";

    public SystemConfigLoader() {
        this(DEFAULT_SYSTEM_PROPERTY_NAME);
    }

    public SystemConfigLoader(String systemPropertyName) {
        super(System.getProperty(systemPropertyName) != null ? new File(System.getProperty(systemPropertyName)) : null);
    }
}
