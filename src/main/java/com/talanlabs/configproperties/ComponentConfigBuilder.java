package com.talanlabs.configproperties;

import com.talanlabs.component.IComponent;
import com.talanlabs.configproperties.loader.IConfigLoader;
import com.talanlabs.configproperties.meta.MetaInfoBeanDelegate;

public class ComponentConfigBuilder<E extends IComponent> {

    private final ConfigBuilder<E> configBuilder;

    private ComponentConfigBuilder(Class<E> componentClass) {
        super();

        configBuilder = ConfigBuilder.newBuilder(componentClass, MetaInfoBeanDelegate::new);
    }

    /**
     * Create config component
     *
     * @param componentClass component class
     */
    public static <E extends IComponent> ComponentConfigBuilder<E> newBuilder(Class<E> componentClass) {
        return new ComponentConfigBuilder<>(componentClass);
    }

    public ComponentConfigBuilder<E> configLoader(IConfigLoader configLoader) {
        this.configBuilder.configLoader(configLoader);
        return this;
    }

    /**
     * Add config property
     */
    public ComponentConfigBuilder<E> configProperty(IConfigProperty configProperty, IConfigProperty... configProperties) {
        this.configBuilder.configProperty(configProperty,configProperties);
        return this;
    }

    /**
     * @return New config component
     */
    public E build() {
        return this.configBuilder.build();
    }
}
