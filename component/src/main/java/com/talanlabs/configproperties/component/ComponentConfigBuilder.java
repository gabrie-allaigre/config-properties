package com.talanlabs.configproperties.component;

import com.talanlabs.component.IComponent;
import com.talanlabs.configproperties.ConfigBuilder;
import com.talanlabs.configproperties.IConfigProperty;
import com.talanlabs.configproperties.component.meta.ComponentMetaInfoBean;
import com.talanlabs.configproperties.loader.IConfigLoader;

public class ComponentConfigBuilder<E extends IComponent> {

    private final ConfigBuilder<E> configBuilder;

    private ComponentConfigBuilder(Class<E> componentClass) {
        super();

        configBuilder = ConfigBuilder.newBuilder(componentClass, ComponentMetaInfoBean.createDelegateMetaInfoBeanFunction());
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
        this.configBuilder.configProperty(configProperty, configProperties);
        return this;
    }

    /**
     * @return New config component
     */
    public E build() {
        return this.configBuilder.build();
    }
}
