package com.talanlabs.configproperties.properties;

import com.talanlabs.configproperties.IConfigProperty;

import java.util.Properties;

public class PropertiesConfigProperty implements IConfigProperty {

    private final String propertyName;

    public PropertiesConfigProperty(String propertyName) {
        super();

        this.propertyName = propertyName;
    }

    @Override
    public void setProperty(Context<?> context, Properties properties) {
        context.setPropertyValue(propertyName, properties);
    }

    @Override
    public String toString() {
        return "All key in " + propertyName;
    }
}
