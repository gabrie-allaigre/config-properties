package com.talanlabs.configproperties;

import com.talanlabs.component.IComponent;

import java.util.Properties;

public interface IConfigProperty<E> {

    /**
     * Set propertie in component
     *
     * @param properties current properties
     * @param component  to set in
     * @return
     */
    E setProperty(Properties properties, IComponent component);
}
