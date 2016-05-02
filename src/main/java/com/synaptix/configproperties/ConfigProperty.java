package com.synaptix.configproperties;

import com.synaptix.component.IComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

public class ConfigProperty<E> {

    private static final IFromString<String> STRING_FROM_STRING = value -> value;
    private static final IFromString<Integer> INTEGER_FROM_STRING = Integer::parseInt;
    private static final IFromString<Long> LONG_FROM_STRING = Long::parseLong;

    private final String key;
    private final String propertyName;
    private final IFromString<E> fromString;
    private final E defaultValue;

    private ConfigProperty(String key, String propertyName, IFromString<E> fromString, E defaultValue) {
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
        config.straightSetProperty(propertyName, v);
        return v;
    }

    public interface IFromString<E> {

        E fromString(String value);

    }
}
