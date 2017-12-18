package com.talanlabs.configproperties.properties;

import com.talanlabs.configproperties.IConfigProperty;
import com.talanlabs.configproperties.utils.ConfigHelper;
import com.talanlabs.configproperties.utils.IFromString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CollectionMultiLinesConfigProperty<E, F extends Collection<E>> implements IConfigProperty {

    public static final String DEFAULT_SEPARATOR = ".";

    private final String key;
    private final String propertyName;
    private final String separator;
    private final Supplier<F> supplier;
    private final IFromString<E> elementFromString;
    private final F defaultValue;

    /**
     * Array config property, default seperator "."
     *
     * @param key               key in properties
     * @param propertyName      name in IComponent
     * @param supplier          create Array
     * @param elementFromString String convert for value
     * @param defaultValue      default collection
     */
    public CollectionMultiLinesConfigProperty(String key, String propertyName, Supplier<F> supplier, IFromString<E> elementFromString, F defaultValue) {
        this(key, propertyName, DEFAULT_SEPARATOR, supplier, elementFromString, defaultValue);
    }

    /**
     * Array config property
     *
     * @param key               key in properties
     * @param propertyName      name in IComponent
     * @param separator         separator
     * @param supplier          create Map
     * @param elementFromString String convert for key
     * @param defaultValue      default map
     */
    public CollectionMultiLinesConfigProperty(String key, String propertyName, String separator, Supplier<F> supplier, IFromString<E> elementFromString, F defaultValue) {
        super();

        this.key = key;
        this.propertyName = propertyName;
        this.separator = separator;
        this.supplier = supplier;
        this.elementFromString = elementFromString;
        this.defaultValue = defaultValue;
    }

    /**
     * Config property for List value ex :
     * - *.1=toto
     * - *.2=tata
     * - *.3=titi
     */
    public static <E> CollectionMultiLinesConfigProperty<E, List<E>> toList(String key, String propertyName, IFromString<E> elementFromString, List<E> defaultValue) {
        return new CollectionMultiLinesConfigProperty<>(key, propertyName, ArrayList::new, elementFromString, defaultValue);
    }

    /**
     * Config property for Set value ex :
     * - *.1=toto
     * - *.2=tata
     * - *.3=titi
     */
    public static <E> CollectionMultiLinesConfigProperty<E, Set<E>> toSet(String key, String propertyName, IFromString<E> elementFromString, Set<E> defaultValue) {
        return new CollectionMultiLinesConfigProperty<>(key, propertyName, HashSet::new, elementFromString, defaultValue);
    }

    /**
     * Config property for Generic value ex :
     * - *.1=toto
     * - *.2=tata
     * - *.3=titi
     */
    public static <E, F extends Collection<E>> CollectionMultiLinesConfigProperty<E, F> toGeneric(String key, String propertyName, Supplier<F> supplier, IFromString<E> elementFromString,
            F defaultValue) {
        return new CollectionMultiLinesConfigProperty<>(key, propertyName, supplier, elementFromString, defaultValue);
    }

    @Override
    public void setProperty(Context<?> context, Properties properties) {
        Properties sp = ConfigHelper.extractProperties(properties, key + separator);
        F col;
        if (sp.isEmpty()) {
            col = defaultValue;
        } else {
            col = sp.stringPropertyNames().stream().sorted().map(sp::getProperty).map(elementFromString::fromString).collect(Collectors.toCollection(supplier));
        }
        context.setPropertyValue(propertyName, col);
    }
}
