package com.synaptix.configproperties.properties;

import com.synaptix.configproperties.ConfigProperty;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CollectionConfigProperty<E, F extends Collection<E>> extends ConfigProperty<F> {

    public static final String DEFAULT_SEPERATOR = ",";

    public CollectionConfigProperty(String key, String propertyName, Supplier<F> supplier, IFromString<E> elementFromString, F defaultValue) {
        this(key, propertyName, DEFAULT_SEPERATOR, supplier, elementFromString, defaultValue);
    }

    public CollectionConfigProperty(String key, String propertyName, String separator, Supplier<F> supplier, IFromString<E> elementFromString, F defaultValue) {
        super(key, propertyName, new CollectionFromString<E, F>(separator, elementFromString, supplier), defaultValue);
    }

    public static <E> ConfigProperty<List<E>> toList(String key, String propertyName, IFromString<E> elementFromString, List<E> defaultValue) {
        return new CollectionConfigProperty<E, List<E>>(key, propertyName, ArrayList::new, elementFromString, defaultValue);
    }

    public static <E> ConfigProperty<Set<E>> toSet(String key, String propertyName, IFromString<E> elementFromString, Set<E> defaultValue) {
        return new CollectionConfigProperty<E, Set<E>>(key, propertyName, HashSet::new, elementFromString, defaultValue);
    }

    private static class CollectionFromString<E, F extends Collection<E>> implements IFromString<F> {

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
            String[] ss = value.split(separator);
            F collection = supplier.get();
            collection.addAll(Arrays.stream(ss).map(elementFromString::fromString).collect(Collectors.toList()));
            return collection;
        }
    }
}
