package com.synaptix.configproperties.loader;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class CompositeConfigLoader implements IConfigLoader {

    private List<IConfigLoader> configLoaders;

    private CompositeConfigLoader(IConfigLoader... configLoaders) {
        super();

        this.configLoaders = Arrays.asList(Objects.requireNonNull(configLoaders));
    }

    @Override
    public Properties readProperties() {
        Properties properties = new Properties();
        configLoaders.stream().map(IConfigLoader::readProperties).filter(p -> p != null).forEach(properties::putAll);
        return properties;
    }
}
