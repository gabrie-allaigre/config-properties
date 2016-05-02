package com.talanlabs.configproperties.properties;

import com.talanlabs.component.IComponent;
import com.talanlabs.configproperties.IConfigProperty;
import com.talanlabs.configproperties.utils.ConfigHelper;
import com.talanlabs.configproperties.utils.IFromString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

public class MapConfigProperty<E, F> implements IConfigProperty<Map<E, F>> {

    public static final String DEFAULT_SEPARATOR = ".";

    private final String key;
    private final String propertyName;
    private final String separator;
    private final Supplier<Map<E, F>> supplier;
    private final IFromString<E> keyFromString;
    private final IFromString<F> valueFromString;
    private final Map<E, F> defaultValue;

    /**
     * Map config property, default seperator "."
     *
     * @param key             key in properties
     * @param propertyName    name in IComponent
     * @param supplier        create Map
     * @param keyFromString   String convert for key
     * @param valueFromString String convert for value
     * @param defaultValue    default map
     */
    public MapConfigProperty(String key, String propertyName, Supplier<Map<E, F>> supplier, IFromString<E> keyFromString, IFromString<F> valueFromString, Map<E, F> defaultValue) {
        this(key, propertyName, DEFAULT_SEPARATOR, supplier, keyFromString, valueFromString, defaultValue);
    }

    /**
     * Map config property
     *
     * @param key             key in properties
     * @param propertyName    name in IComponent
     * @param separator       separator
     * @param supplier        create Map
     * @param keyFromString   String convert for key
     * @param valueFromString String convert for value
     * @param defaultValue    default map
     */
    public MapConfigProperty(String key, String propertyName, String separator, Supplier<Map<E, F>> supplier, IFromString<E> keyFromString, IFromString<F> valueFromString, Map<E, F> defaultValue) {
        super();

        this.key = key;
        this.propertyName = propertyName;
        this.separator = separator;
        this.supplier = supplier;
        this.keyFromString = keyFromString;
        this.valueFromString = valueFromString;
        this.defaultValue = defaultValue;
    }

    /**
     * Config property for HashMap value ex :
     * <pre>
     * server.A=10
     * server.B=20
     * </pre>
     */
    public static <E, F> MapConfigProperty<E, F> toHashMap(String key, String propertyName, IFromString<E> keyFromString, IFromString<F> valueFromString, Map<E, F> defaultValue) {
        return new MapConfigProperty<E, F>(key, propertyName, HashMap::new, keyFromString, valueFromString, defaultValue);
    }

    @Override
    public Map<E, F> setProperty(Properties properties, IComponent component) {
        Map<E, F> map = properties.stringPropertyNames().stream().filter(p -> p.startsWith(key + separator) && p.length() > (key.length() + separator.length())).map(p -> set(properties, p))
                .collect(supplier, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
        ConfigHelper.setPropertyValue(component, propertyName, map);
        return map;
    }

    private Map.Entry<E, F> set(Properties properties, String p) {
        String s1 = p.substring(key.length() + separator.length());
        E k = keyFromString.fromString(s1);

        String s2 = properties.getProperty(p);
        F v;
        if (StringUtils.isBlank(s2)) {
            v = defaultValue != null ? defaultValue.get(k) : null;
        } else {
            v = valueFromString.fromString(s2);
        }
        return Pair.of(k, v);
    }
}
