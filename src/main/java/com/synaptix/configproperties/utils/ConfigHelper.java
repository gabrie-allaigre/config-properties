package com.synaptix.configproperties.utils;

import com.synaptix.component.IComponent;
import com.synaptix.component.factory.ComponentFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigHelper {

    private static final Logger LOG = LogManager.getLogger(ConfigHelper.class);

    private ConfigHelper() {
        super();
    }

    /**
     * Set value in component, create sub component
     *
     * @param component
     * @param propertyName
     * @param value
     */
    public static final <E extends IComponent, F> void setPropertyValue(E component, String propertyName, F value) {
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
}
