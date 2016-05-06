package com.synaptix.configproperties.properties;

import com.synaptix.configproperties.utils.IFromString;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ArrayConfigProperty<E> extends ConfigProperty<E[]> {

    public static final String DEFAULT_SEPARATOR = ",";

    /**
     * Collection config property, default seperator ","
     *
     * @param key               key in properties
     * @param propertyName      name in IComponent
     * @param supplier          create Collection
     * @param elementFromString String convert for element
     * @param defaultValue      default value
     */
    public ArrayConfigProperty(String key, String propertyName, Supplier<E[]> supplier, IFromString<E> elementFromString, E[] defaultValue) {
        this(key, propertyName, DEFAULT_SEPARATOR, supplier, elementFromString, defaultValue);
    }

    /**
     * Collection config property
     *
     * @param key               key in properties
     * @param propertyName      name in IComponent
     * @param separator         seperator, use regex
     * @param supplier          create Collection
     * @param elementFromString String convert for element
     * @param defaultValue      default value
     */
    public ArrayConfigProperty(String key, String propertyName, String separator, Supplier<E[]> supplier, IFromString<E> elementFromString, E[] defaultValue) {
        super(key, propertyName, new ArrayFromString<E>(separator, elementFromString, supplier), defaultValue);
    }

    /**
     * Config property for Array String value
     */
    public static ConfigProperty<String[]> toArrayString(String key, String propertyName, String[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, () -> new String[0], ConfigProperty.STRING_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Integer value
     */
    public static ConfigProperty<Integer[]> toArrayInteger(String key, String propertyName, Integer[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, () -> new Integer[0], ConfigProperty.INTEGER_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Double value
     */
    public static ConfigProperty<Double[]> toArrayDouble(String key, String propertyName, Double[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, () -> new Double[0], ConfigProperty.DOUBLE_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Float value
     */
    public static ConfigProperty<Float[]> toArrayFloat(String key, String propertyName, Float[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, () -> new Float[0], ConfigProperty.FLOAT_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Long value
     */
    public static ConfigProperty<Long[]> toArrayLong(String key, String propertyName, Long[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, () -> new Long[0], ConfigProperty.LONG_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Boolean value
     */
    public static ConfigProperty<Boolean[]> toArrayBoolean(String key, String propertyName, Boolean[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, () -> new Boolean[0], ConfigProperty.BOOLEAN_FROM_STRING, defaultValue);
    }

    /**
     * Config property for Array Generic value
     */
    public static <E> ConfigProperty<E[]> toArrayGeneric(String key, String propertyName, Supplier<E[]> supplier, IFromString<E> elementFromString, E[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, supplier, elementFromString, defaultValue);
    }

    public static class ArrayFromString<E> implements IFromString<E[]> {

        private final String separator;
        private final IFromString<E> elementFromString;
        private final Supplier<E[]> supplier;

        public ArrayFromString(String separator, IFromString<E> elementFromString, Supplier<E[]> supplier) {
            super();

            this.separator = separator;
            this.elementFromString = elementFromString;
            this.supplier = supplier;
        }

        @Override
        public E[] fromString(String value) {
            return Arrays.stream(value.split(separator)).map(elementFromString::fromString).collect(Collectors.toList()).toArray(supplier.get());
        }
    }
}
