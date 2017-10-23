package com.talanlabs.configproperties;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.configproperties.loader.FirstNotNullConfigLoader;
import com.talanlabs.configproperties.loader.IConfigLoader;
import com.talanlabs.configproperties.loader.ResourceConfigLoader;
import com.talanlabs.configproperties.loader.SystemConfigLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigBuilder<E extends IComponent> {

    private static final Logger LOG = LogManager.getLogger(ConfigBuilder.class);

    private final Class<E> componentClass;
    private final List<IConfigProperty<?>> configProperties;

    private IConfigLoader configLoader;

    private ConfigBuilder(Class<E> componentClass) {
        super();

        this.componentClass = componentClass;
        this.configProperties = new ArrayList<>();
    }

    /**
     * Create config component
     *
     * @param componentClass component class
     */
    public static <E extends IComponent> ConfigBuilder<E> newBuilder(Class<E> componentClass) {
        return new ConfigBuilder<>(componentClass);
    }

    /**
     * @param configLoader change config loader, default is DefaultConfigLoader
     */
    public ConfigBuilder configLoader(IConfigLoader configLoader) {
        this.configLoader = configLoader;
        return this;
    }

    /**
     * Add config property
     */
    public ConfigBuilder configProperty(IConfigProperty<?> configProperty, IConfigProperty<?>... configProperties) {
        this.configProperties.add(configProperty);
        if (configProperties != null) {
            this.configProperties.addAll(Arrays.asList(configProperties));
        }
        return this;
    }

    /**
     * @return New config component
     */
    public E build() {
        if (configLoader == null) {
            configLoader = new FirstNotNullConfigLoader(new SystemConfigLoader(), new ResourceConfigLoader("config.properties"), new ResourceConfigLoader("config.yml"));
        }

        Properties properties = configLoader.readProperties();
        E config = ComponentFactory.getInstance().createInstance(componentClass);
        configProperties.forEach(configProperty -> setProperty(configProperty, properties, config));
        return config;
    }

    private void setProperty(IConfigProperty<?> configProperty, Properties properties, E component) {
        Object res = configProperty.setProperty(properties, component);
        LOG.info(String.format("Set property %1s -> %2s", configProperty.toString(), res));
    }
}
