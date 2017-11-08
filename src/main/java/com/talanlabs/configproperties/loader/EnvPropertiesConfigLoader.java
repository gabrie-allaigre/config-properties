package com.talanlabs.configproperties.loader;

import java.util.AbstractMap;
import java.util.Properties;
import java.util.function.Function;

public class EnvPropertiesConfigLoader implements IConfigLoader {

    private Function<String, String> keyTransformer;

    public EnvPropertiesConfigLoader() {
        this("_", ".");
    }

    public EnvPropertiesConfigLoader(String regex, String replacement) {
        this(k -> k.replaceAll(regex, replacement));
    }

    public EnvPropertiesConfigLoader(Function<String, String> keyTransformer) {
        super();

        this.keyTransformer = keyTransformer;
    }

    @Override
    public Properties readProperties() {
        return System.getenv().entrySet().stream().map(e -> new AbstractMap.SimpleEntry<>(keyTransformer.apply(e.getKey()), e.getValue())).collect(Properties::new, (p, e) -> p.setProperty(e.getKey(), e.getValue()), Properties::putAll);
    }
}
