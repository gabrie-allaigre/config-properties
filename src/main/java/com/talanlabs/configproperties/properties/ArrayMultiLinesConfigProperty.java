package com.talanlabs.configproperties.properties;

import com.talanlabs.component.IComponent;
import com.talanlabs.configproperties.IConfigProperty;
import com.talanlabs.configproperties.utils.ConfigHelper;
import com.talanlabs.configproperties.utils.IFromString;

import java.util.Properties;
import java.util.function.IntFunction;

public class ArrayMultiLinesConfigProperty<E> implements IConfigProperty<E[]> {

    public static final String DEFAULT_SEPARATOR = ".";

    private final String key;
    private final String propertyName;
    private final String separator;
    private final IntFunction<E[]> intFunction;
    private final IFromString<E> elementFromString;
    private final E[] defaultValue;

    /**
     * Array config property, default seperator "."
     *
     * @param key               key in properties
     * @param propertyName      name in IComponent
     * @param intFunction       create Array
     * @param elementFromString String convert for value
     * @param defaultValue      default array
     */
    public ArrayMultiLinesConfigProperty(String key, String propertyName, IntFunction<E[]> intFunction, IFromString<E> elementFromString, E[] defaultValue) {
        this(key, propertyName, DEFAULT_SEPARATOR, intFunction, elementFromString, defaultValue);
    }

    /**
     * Array config property
     *
     * @param key               key in properties
     * @param propertyName      name in IComponent
     * @param separator         separator
     * @param intFunction       create Map
     * @param elementFromString String convert for key
     * @param defaultValue      default map
     */
    public ArrayMultiLinesConfigProperty(String key, String propertyName, String separator, IntFunction<E[]> intFunction, IFromString<E> elementFromString, E[] defaultValue) {
        super();

        this.key = key;
        this.propertyName = propertyName;
        this.separator = separator;
        this.intFunction = intFunction;
        this.elementFromString = elementFromString;
        this.defaultValue = defaultValue;
    }

    /**
     * Config property for Array String value
     */
    public static ArrayMultiLinesConfigProperty<String> toArrayString(String key, String propertyName, String[] defaultValue) {
        return new ArrayMultiLinesConfigProperty<>(key, propertyName, String[]::new, ConfigProperty.STRING_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Integer value
     */
    public static ArrayMultiLinesConfigProperty<Integer> toArrayInteger(String key, String propertyName, Integer[] defaultValue) {
        return new ArrayMultiLinesConfigProperty<>(key, propertyName, Integer[]::new, ConfigProperty.INTEGER_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Double value
     */
    public static ArrayMultiLinesConfigProperty<Double> toArrayDouble(String key, String propertyName, Double[] defaultValue) {
        return new ArrayMultiLinesConfigProperty<>(key, propertyName, Double[]::new, ConfigProperty.DOUBLE_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Float value
     */
    public static ArrayMultiLinesConfigProperty<Float> toArrayFloat(String key, String propertyName, Float[] defaultValue) {
        return new ArrayMultiLinesConfigProperty<>(key, propertyName, Float[]::new, ConfigProperty.FLOAT_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Long value
     */
    public static ArrayMultiLinesConfigProperty<Long> toArrayLong(String key, String propertyName, Long[] defaultValue) {
        return new ArrayMultiLinesConfigProperty<>(key, propertyName, Long[]::new, ConfigProperty.LONG_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Boolean value
     */
    public static ArrayMultiLinesConfigProperty<Boolean> toArrayBoolean(String key, String propertyName, Boolean[] defaultValue) {
        return new ArrayMultiLinesConfigProperty<>(key, propertyName, Boolean[]::new, ConfigProperty.BOOLEAN_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array value ex :
     * <pre>
     * server.0=10
     * server.1=20
     * </pre>
     */
    public static <E> ArrayMultiLinesConfigProperty<E> toArrayGeneric(String key, String propertyName, IntFunction<E[]> intFunction, IFromString<E> elementFromString, E[] defaultValue) {
        return new ArrayMultiLinesConfigProperty<>(key, propertyName, intFunction, elementFromString, defaultValue);
    }

    @Override
    public E[] setProperty(Properties properties, IComponent component) {
        Properties sp = ConfigHelper.extractProperties(properties, key + separator);
        if (sp.isEmpty()) {
            return defaultValue;
        }
        E[] array = sp.stringPropertyNames().stream().sorted().map(sp::getProperty).map(elementFromString::fromString).toArray(intFunction);
        ConfigHelper.setPropertyValue(component, propertyName, array);
        return array;
    }
}
