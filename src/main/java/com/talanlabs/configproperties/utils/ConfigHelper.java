package com.talanlabs.configproperties.utils;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.factory.ComponentFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class ConfigHelper {

    private static final Logger LOG = LogManager.getLogger(ConfigHelper.class);

    private ConfigHelper() {
        super();
    }

    /**
     * Set value in component, create sub component
     *
     * @param component    root component
     * @param propertyName property with dot
     * @param value        value of property
     */
    public static <E extends IComponent, F> void setPropertyValue(E component, String propertyName, F value) {
        if (component == null) {
            return;
        }
        int index = propertyName.indexOf('.');
        if (index == -1) {
            component.straightSetProperty(propertyName, value);
        } else {
            String head = propertyName.substring(0, index);
            Object temp = component.straightGetProperty(head);
            if (temp == null) {
                Class<?> clazz = component.straightGetPropertyClass(head);
                if (!ComponentFactory.getInstance().isComponentType(clazz)) {
                    LOG.error("Not set property " + propertyName + " Parent is not IComponent " + head);
                    return;
                }
                temp = ComponentFactory.getInstance().createInstance(clazz);
                component.straightSetProperty(head, temp);
            } else if (!(temp instanceof IComponent)) {
                LOG.error("Not set property " + propertyName + " Parent is not IComponent " + head);
                return;
            }
            setPropertyValue((IComponent) temp, propertyName.substring(index + 1), value);
        }
    }

    /**
     * Extract in properties sub properties
     *
     * @param properties properties with sub properties
     * @param prefix     prefix to remove
     * @return new properties
     */
    public static Properties extractProperties(Properties properties, String prefix) {
        return properties.entrySet().stream().filter(e -> {
            String p = (String) e.getKey();
            return p.startsWith(prefix) && p.length() > prefix.length();
        }).collect(Properties::new, (a, b) -> a.put(b.getKey().toString().substring(prefix.length()), b.getValue()), Properties::putAll);
    }
}
