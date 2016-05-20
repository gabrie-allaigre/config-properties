package com.synaptix.configproperties.loader;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class ComposeConfigLoader implements IConfigLoader {

    private final List<IConfigLoader> configLoaders;

    public ComposeConfigLoader(IConfigLoader... configLoaders) {
        super();

        this.configLoaders = Arrays.asList(Objects.requireNonNull(configLoaders));
    }

    @Override
    public Properties readProperties() {
        List<Properties> propertiess = configLoaders.stream().map(IConfigLoader::readProperties).filter(p -> p != null).collect(Collectors.toList());
        if (propertiess != null && !propertiess.isEmpty()) {
            Properties properties = new Properties();
            propertiess.stream().forEach(properties::putAll);
            return properties;
        }
        return null;
    }
}
