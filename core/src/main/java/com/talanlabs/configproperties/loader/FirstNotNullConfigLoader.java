package com.talanlabs.configproperties.loader;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

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
