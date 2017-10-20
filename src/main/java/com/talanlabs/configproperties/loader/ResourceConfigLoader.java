package com.talanlabs.configproperties.loader;

public class ResourceConfigLoader extends URLConfigLoader {

    public static final String DEFAULT_INTERNAL_PROPERTIES_PATH = "config.properties";

    public ResourceConfigLoader() {
        this(DEFAULT_INTERNAL_PROPERTIES_PATH);
    }

    public ResourceConfigLoader(String internalPropertiesPath) {
        super();

        setUrl(this.getClass().getClassLoader().getResource(internalPropertiesPath));
    }
}
