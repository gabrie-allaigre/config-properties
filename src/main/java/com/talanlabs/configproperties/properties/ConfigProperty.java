package com.talanlabs.configproperties.properties;

import com.talanlabs.component.IComponent;
import com.talanlabs.configproperties.IConfigProperty;
import com.talanlabs.configproperties.utils.ConfigHelper;
import com.talanlabs.configproperties.utils.IFromString;

import java.util.Properties;

public class ConfigProperty<E> implements IConfigProperty<E> {

    public static final IFromString<String> STRING_FROM_STRING = value -> value;
    public static final IFromString<Integer> INTEGER_FROM_STRING = Integer::parseInt;
    public static final IFromString<Long> LONG_FROM_STRING = Long::parseLong;
    public static final IFromString<Float> FLOAT_FROM_STRING = Float::parseFloat;
    public static final IFromString<Double> DOUBLE_FROM_STRING = Double::parseDouble;
    public static final IFromString<Boolean> BOOLEAN_FROM_STRING = Boolean::parseBoolean;

    private final String key;
    private final String propertyName;
    private final IFromString<E> fromString;
    private final E defaultValue;

    /**
     * @param key          key in properties
     * @param propertyName name in IComponent
     * @param fromString   String convert
     * @param defaultValue default value
     */
    public ConfigProperty(String key, String propertyName, IFromString<E> fromString, E defaultValue) {
        super();

        this.key = key;
        this.propertyName = propertyName;
        this.fromString = fromString;
        this.defaultValue = defaultValue;
    }

    /**
     * Config property for String value
     */
    public static ConfigProperty<String> toString(String key, String propertyName, String defaultValue) {
        return toGeneric(key, propertyName, STRING_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Long value
     */
    public static ConfigProperty<Long> toLong(String key, String propertyName, Long defaultValue) {
        return toGeneric(key, propertyName, LONG_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Integer value
     */
    public static ConfigProperty<Integer> toInteger(String key, String propertyName, Integer defaultValue) {
        return toGeneric(key, propertyName, INTEGER_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Float value
     */
    public static ConfigProperty<Float> toFloat(String key, String propertyName, Float defaultValue) {
        return toGeneric(key, propertyName, FLOAT_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Double value
     */
    public static ConfigProperty<Double> toDouble(String key, String propertyName, Double defaultValue) {
        return toGeneric(key, propertyName, DOUBLE_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Boolean value
     */
    public static ConfigProperty<Boolean> toBoolean(String key, String propertyName, Boolean defaultValue) {
        return toGeneric(key, propertyName, BOOLEAN_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Generic value
     */
    public static <E> ConfigProperty<E> toGeneric(String key, String propertyName, IFromString<E> fromString, E defaultValue) {
        return new ConfigProperty<E>(key, propertyName, fromString, defaultValue);
    }

    @Override
    public E setProperty(Properties properties, IComponent component) {
        E v;
        if (!properties.containsKey(key)) {
            v = defaultValue;
        } else {
            String s = properties.getProperty(key);
            v = fromString.fromString(s);
        }
        ConfigHelper.setPropertyValue(component, propertyName, v);
        return v;
    }

    @Override
    public String toString() {
        return key + " in " + propertyName;
    }

}
