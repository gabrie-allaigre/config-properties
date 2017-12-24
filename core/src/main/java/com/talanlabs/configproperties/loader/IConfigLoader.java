package com.talanlabs.configproperties.loader;

import java.io.File;
import java.net.URL;
import java.util.Properties;

public interface IConfigLoader {

    /**
     * Get a config loader with properties
     *
     * @param properties a properties
     * @return a config loader
     */
    static IConfigLoader properties(Properties properties) {
        return new PropertiesConfigLoader(properties);
    }

    /**
     * Get a config loader with main args. Use StaticGetArgs.getINSTANCE().setArgs
     *
     * @param args argument
     * @return a config loader
     */
    static IConfigLoader args(String[] args) {
        return new ArgsConfigLoader(args);
    }

    /**
     * Get a config loader with file properties
     *
     * @param file file
     * @return a config loader
     */
    static IConfigLoader file(File file) {
        return new FileConfigLoader(file);
    }

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
     * Get a config loader with system properties
     *
     * @return a config loader
     */
    static IConfigLoader systemProperties() {
        return new SystemPropertiesConfigLoader();
    }

    /**
     * Get a config loader with env properties
     *
     * @return a config loader
     */
    static IConfigLoader envProperties() {
        return new EnvPropertiesConfigLoader();
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
