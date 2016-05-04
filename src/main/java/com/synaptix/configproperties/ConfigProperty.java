package com.synaptix.configproperties;

import com.synaptix.component.IComponent;
import com.synaptix.component.factory.ComponentFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class ConfigProperty<E> {

    private static final Logger LOG = LogManager.getLogger(ConfigProperty.class);

    private static final IFromString<String> STRING_FROM_STRING = value -> value;
    private static final IFromString<Integer> INTEGER_FROM_STRING = Integer::parseInt;
    private static final IFromString<Long> LONG_FROM_STRING = Long::parseLong;

    private final String key;
    private final String propertyName;
    private final IFromString<E> fromString;
    private final E defaultValue;

    public ConfigProperty(String key, String propertyName, IFromString<E> fromString, E defaultValue) {
        super();

        this.key = key;
        this.propertyName = propertyName;
        this.fromString = fromString;
        this.defaultValue = defaultValue;
    }

    public static ConfigProperty<String> toString(String key, String propertyName, String defaultValue) {
        return toGeneric(key, propertyName, STRING_FROM_STRING, defaultValue);
    }

    public static ConfigProperty<Long> toLong(String key, String propertyName, Long defaultValue) {
        return toGeneric(key, propertyName, LONG_FROM_STRING, defaultValue);
    }

    public static ConfigProperty<Integer> toInteger(String key, String propertyName, Integer defaultValue) {
        return toGeneric(key, propertyName, INTEGER_FROM_STRING, defaultValue);
    }

    public static <E> ConfigProperty<E> toGeneric(String key, String propertyName, IFromString<E> fromString, E defaultValue) {
        return new ConfigProperty<E>(key, propertyName, fromString, defaultValue);
    }

    public String getKey() {
        return key;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public IFromString<E> getFromString() {
        return fromString;
    }

    public E getDefaultValue() {
        return defaultValue;
    }

    public E setProperty(Properties properties, IComponent config) {
        String s = properties.getProperty(key);
        E v;
        if (StringUtils.isBlank(s)) {
            v = defaultValue;
        } else {
            v = fromString.fromString(s);
        }
        setValue(config, propertyName, v);
        return v;
    }

    private void setValue(IComponent component, String propertyName, E value) {
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
            setValue((IComponent) temp, propertyName.substring(index + 1), value);
        }
    }


    public interface IFromString<E> {

        E fromString(String value);

    }
}
