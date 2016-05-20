package com.synaptix.configproperties.loader;

import java.net.URL;
import java.util.Properties;

public interface IConfigLoader {

    /**
     * Get a config loader with system properties path
     *
     * @param propertyName system property name
     * @return a config loader
     */
    static IConfigLoader system(String propertyName) {
        return new SystemConfigLoader(propertyName);
    }

    /**
     * Get a config loader with url
     *
     * @param url a url
     * @return a config loader
     */
    static IConfigLoader url(URL url) {
        return new URLConfigLoader(url);
    }

    /**
     * Get a config loader with internal resource
     *
     * @param internalPath resource path
     * @return a config loader
     */
    static IConfigLoader resource(String internalPath) {
        return new ResourceConfigLoader(internalPath);
    }

    /**
     * Compose a list of config loader
     *
     * @param configLoaders List of config loader
     * @return Config loader
     */
    static IConfigLoader compose(IConfigLoader... configLoaders) {
        return new ComposeConfigLoader(configLoaders);
    }

    /**
     * Get a first config loader not null
     *
     * @param configLoaders List of config loader
     * @return Config loader
     */
    static IConfigLoader firstNotNull(IConfigLoader... configLoaders) {
        return new FirstNotNullConfigLoader(configLoaders);
    }

    /**
     * Load config properties
     *
     * @return
     */
    Properties readProperties();
}
