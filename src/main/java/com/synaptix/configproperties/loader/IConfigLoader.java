package com.synaptix.configproperties.loader;

import java.util.Properties;

public interface IConfigLoader {

    /**
     * Load config properties
     *
     * @return
     */
    Properties readProperties();
}
