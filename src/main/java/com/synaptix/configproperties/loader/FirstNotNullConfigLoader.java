package com.synaptix.configproperties.loader;

import java.util.*;

public class FirstNotNullConfigLoader implements IConfigLoader {

    private final List<IConfigLoader> configLoaders;

    public FirstNotNullConfigLoader(IConfigLoader... configLoaders) {
        super();

        this.configLoaders = Arrays.asList(Objects.requireNonNull(configLoaders));
    }

    @Override
    public Properties readProperties() {
        Optional<Properties> o = configLoaders.stream().map(IConfigLoader::readProperties).filter(p -> p != null).findFirst();
        return o.isPresent() ? o.get() : null;
    }
}
