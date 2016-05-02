package com.talanlabs.configproperties.properties;

import com.talanlabs.component.IComponent;
import com.talanlabs.configproperties.IConfigProperty;
import com.talanlabs.configproperties.utils.ConfigHelper;

import java.util.Properties;

public class PropertiesConfigProperty implements IConfigProperty<Properties> {

    private final String propertyName;

    public PropertiesConfigProperty(String propertyName) {
        super();

        this.propertyName = propertyName;
    }

    @Override
    public Properties setProperty(Properties properties, IComponent component) {
        ConfigHelper.setPropertyValue(component, propertyName, properties);
        return properties;
    }

    @Override
    public String toString() {
        return "All key in " + propertyName;
    }
}
