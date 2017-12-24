package com.talanlabs.configproperties.properties;

import com.talanlabs.configproperties.utils.IFromString;

import java.util.Arrays;
import java.util.function.IntFunction;

public class ArrayConfigProperty<E> extends ConfigProperty<E[]> {

    public static final String DEFAULT_SEPARATOR = ",";

    /**
     * Collection config property, default seperator ","
     *
     * @param key               key in properties
     * @param propertyName      name in IComponent
     * @param intFunction       create Collection
     * @param elementFromString String convert for element
     * @param defaultValue      default value
     */
    public ArrayConfigProperty(String key, String propertyName, IntFunction<E[]> intFunction, IFromString<E> elementFromString, E[] defaultValue) {
        this(key, propertyName, DEFAULT_SEPARATOR, intFunction, elementFromString, defaultValue);
    }

    /**
     * Collection config property
     *
     * @param key               key in properties
     * @param propertyName      name in IComponent
     * @param separator         seperator, use regex
     * @param intFunction       create Collection
     * @param elementFromString String convert for element
     * @param defaultValue      default value
     */
    public ArrayConfigProperty(String key, String propertyName, String separator, IntFunction<E[]> intFunction, IFromString<E> elementFromString, E[] defaultValue) {
        super(key, propertyName, new ArrayFromString<>(separator, elementFromString, intFunction), defaultValue);
    }

    /**
     * Config property for Array String value
     */
    public static ArrayConfigProperty<String> toArrayString(String key, String propertyName, String[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, String[]::new, STRING_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Integer value
     */
    public static ArrayConfigProperty<Integer> toArrayInteger(String key, String propertyName, Integer[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, Integer[]::new, INTEGER_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Double value
     */
    public static ArrayConfigProperty<Double> toArrayDouble(String key, String propertyName, Double[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, Double[]::new, DOUBLE_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Float value
     */
    public static ArrayConfigProperty<Float> toArrayFloat(String key, String propertyName, Float[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, Float[]::new, FLOAT_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Long value
     */
    public static ArrayConfigProperty<Long> toArrayLong(String key, String propertyName, Long[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, Long[]::new, LONG_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Boolean value
     */
    public static ArrayConfigProperty<Boolean> toArrayBoolean(String key, String propertyName, Boolean[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, Boolean[]::new, BOOLEAN_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Generic value
     */
    public static <E> ArrayConfigProperty<E> toArrayGeneric(String key, String propertyName, IntFunction<E[]> intFunction, IFromString<E> elementFromString, E[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, intFunction, elementFromString, defaultValue);
    }

    public static class ArrayFromString<E> implements IFromString<E[]> {

        private final String separator;
        private final IFromString<E> elementFromString;
        private final IntFunction<E[]> intFunction;

        public ArrayFromString(String separator, IFromString<E> elementFromString, IntFunction<E[]> intFunction) {
            super();

            this.separator = separator;
            this.elementFromString = elementFromString;
            this.intFunction = intFunction;
        }

        @Override
        public E[] fromString(String value) {
            return Arrays.stream(value.split(separator)).map(elementFromString::fromString).toArray(intFunction);
        }
    }
}
