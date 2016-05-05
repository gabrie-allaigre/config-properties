package com.synaptix.configproperties.properties;

import com.synaptix.configproperties.ConfigProperty;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ArrayConfigProperty<E> extends ConfigProperty<E[]> {

    public static final String DEFAULT_SEPERATOR = ",";

    public ArrayConfigProperty(String key, String propertyName, Supplier<E[]> supplier, IFromString<E> elementFromString, E[] defaultValue) {
        this(key, propertyName, DEFAULT_SEPERATOR, supplier, elementFromString, defaultValue);
    }

    public ArrayConfigProperty(String key, String propertyName, String separator, Supplier<E[]> supplier, IFromString<E> elementFromString, E[] defaultValue) {
        super(key, propertyName, new ArrayFromString<E>(separator, elementFromString, supplier), defaultValue);
    }

    public static ConfigProperty<String[]> toArrayString(String key, String propertyName, String[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, () -> new String[0], ConfigProperty.STRING_FROM_STRING, defaultValue);
    }

    public static ConfigProperty<Integer[]> toArrayInteger(String key, String propertyName, Integer[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, () -> new Integer[0], ConfigProperty.INTEGER_FROM_STRING, defaultValue);
    }

    public static ConfigProperty<Double[]> toArrayDouble(String key, String propertyName, Double[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, () -> new Double[0], ConfigProperty.DOUBLE_FROM_STRING, defaultValue);
    }

    public static ConfigProperty<Float[]> toArrayFloat(String key, String propertyName, Float[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, () -> new Float[0], ConfigProperty.FLOAT_FROM_STRING, defaultValue);
    }

    public static ConfigProperty<Long[]> toArrayLong(String key, String propertyName, Long[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, () -> new Long[0], ConfigProperty.LONG_FROM_STRING, defaultValue);
    }

    public static ConfigProperty<Boolean[]> toArrayBoolean(String key, String propertyName, Boolean[] defaultValue) {
        return new ArrayConfigProperty<>(key, propertyName, () -> new Boolean[0], ConfigProperty.BOOLEAN_FROM_STRING, defaultValue);
    }

    private static class ArrayFromString<E> implements IFromString<E[]> {

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
            String[] ss = value.split(separator);
            return Arrays.stream(ss).map(elementFromString::fromString).collect(Collectors.toList()).toArray(supplier.get());
        }
    }
}
