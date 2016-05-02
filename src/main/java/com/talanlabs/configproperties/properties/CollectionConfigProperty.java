package com.talanlabs.configproperties.properties;

import com.talanlabs.configproperties.utils.IFromString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CollectionConfigProperty<E, F extends Collection<E>> extends ConfigProperty<F> {

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
    public CollectionConfigProperty(String key, String propertyName, Supplier<F> supplier, IFromString<E> elementFromString, F defaultValue) {
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
    public CollectionConfigProperty(String key, String propertyName, String separator, Supplier<F> supplier, IFromString<E> elementFromString, F defaultValue) {
        super(key, propertyName, new CollectionFromString<E, F>(separator, elementFromString, supplier), defaultValue);
    }

    /**
     * Config property for List value ex : 1,2,3,4,5
     * Default separator ","
     */
    public static <E> ConfigProperty<List<E>> toList(String key, String propertyName, IFromString<E> elementFromString, List<E> defaultValue) {
        return new CollectionConfigProperty<E, List<E>>(key, propertyName, ArrayList::new, elementFromString, defaultValue);
    }

    /**
     * Config property for Set value ex : 1,2,3,4,5
     * Default separator ","
     */
    public static <E> ConfigProperty<Set<E>> toSet(String key, String propertyName, IFromString<E> elementFromString, Set<E> defaultValue) {
        return new CollectionConfigProperty<E, Set<E>>(key, propertyName, HashSet::new, elementFromString, defaultValue);
    }

    public static class CollectionFromString<E, F extends Collection<E>> implements IFromString<F> {

        private final String separator;
        private final IFromString<E> elementFromString;
        private final Supplier<F> supplier;

        public CollectionFromString(String separator, IFromString<E> elementFromString, Supplier<F> supplier) {
            super();

            this.separator = separator;
            this.elementFromString = elementFromString;
            this.supplier = supplier;
        }

        @Override
        public F fromString(String value) {
            return Arrays.stream(value.split(separator)).map(elementFromString::fromString).collect(Collectors.toCollection(supplier));
        }
    }
}
