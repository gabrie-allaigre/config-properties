package com.talanlabs.configproperties.loader;

import com.google.common.io.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourceConfigLoader extends URLConfigLoader {

    private static final Logger LOG = LogManager.getLogger(ResourceConfigLoader.class);

    public static final String DEFAULT_INTERNAL_PROPERTIES_PATH = "config.properties";

    public ResourceConfigLoader() {
        this(DEFAULT_INTERNAL_PROPERTIES_PATH);
    }

    public ResourceConfigLoader(String internalPropertiesPath) {
        super(Resources.getResource(internalPropertiesPath));
    }
}
