package com.talanlabs.configproperties.utils;

import java.util.Properties;

public class ConfigHelper {

    private ConfigHelper() {
        super();
    }


    /**
     * Extract in properties sub properties
     *
     * @param properties properties with sub properties
     * @param prefix     prefix to remove
     * @return new properties
     */
    public static Properties extractProperties(Properties properties, String prefix) {
        return properties.entrySet().stream().filter(e -> {
            String p = (String) e.getKey();
            return p.startsWith(prefix) && p.length() > prefix.length();
        }).collect(Properties::new, (a, b) -> a.put(b.getKey().toString().substring(prefix.length()), b.getValue()), Properties::putAll);
    }

    /**
     *
     * @param properties
     * @param prefix
     * @return
     */
    public static boolean hasPrefixKey(Properties properties, String prefix) {
        return properties.entrySet().stream().anyMatch(e -> {
            String p = (String) e.getKey();
            return p.startsWith(prefix) && p.length() > prefix.length();
        });
    }
}
