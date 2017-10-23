package com.talanlabs.configproperties.properties;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.factory.ComponentDescriptor;
import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.configproperties.IConfigProperty;
import com.talanlabs.configproperties.properties.autoconfig.DefaultPropertyValue;
import com.talanlabs.configproperties.properties.autoconfig.PropertyKey;
import com.talanlabs.configproperties.utils.ConfigHelper;
import com.talanlabs.rtext.Rtext;
import com.talanlabs.rtext.configuration.IRtextConfiguration;
import com.talanlabs.rtext.configuration.RtextConfigurationBuilder;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoConfigProperty implements IConfigProperty<IComponent> {

    private String prefix;
    private Rtext rtext;

    private AutoConfigProperty() {
        super();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public IComponent setProperty(Properties properties, IComponent component) {
        ComponentDescriptor<?> cd = ComponentFactory.getInstance().getDescriptor(component);
        setProperty(properties, component, cd, "", prefix, false);
        return component;
    }

    private void setProperty(Properties properties, IComponent component, ComponentDescriptor<?> cd, String componentPrefix, String propertyPrefix, boolean ignoreDefault) {
        cd.getPropertyDescriptors().forEach(pd -> set(properties, component, componentPrefix, propertyPrefix, pd, ignoreDefault));
    }

    private void set(Properties properties, IComponent component, String componentPrefix, String propertyPrefix, ComponentDescriptor.PropertyDescriptor pd, boolean ignoreDefault) {
        String componentKey = (StringUtils.isNotBlank(componentPrefix) ? componentPrefix + "." : "") + pd.getPropertyName();

        PropertyKey pk = pd.getMethod().getAnnotation(PropertyKey.class);
        if (pk != null && pk.ignore()) {
            return;
        }

        String propertyKey = (StringUtils.isNotBlank(propertyPrefix) ? propertyPrefix + "." : "");
        if (pk != null && StringUtils.isNotEmpty(pk.value())) {
            propertyKey += pk.value();
        } else {
            propertyKey += pd.getPropertyName();
        }

        if (ComponentFactory.getInstance().isComponentType(pd.getPropertyType())) {
            setComponent(properties, component, componentKey, propertyKey, pd, ignoreDefault, pk);
        } else if (Properties.class.isAssignableFrom(pd.getPropertyClass())) {
            ConfigHelper.setPropertyValue(component, componentKey, properties);
        } else {
            setValue(properties, component, componentKey, propertyKey, pd, ignoreDefault, pk);
        }
    }

    private void setComponent(Properties properties, IComponent component, String componentKey, String propertyKey, ComponentDescriptor.PropertyDescriptor pd, boolean ignoreDefault, PropertyKey pk) {
        setProperty(properties, component, ComponentFactory.getInstance().getDescriptor(pd.getPropertyType()), componentKey, propertyKey, ignoreDefault);

        if (pk != null && StringUtils.isNotEmpty(pk.alternative())) {
            setProperty(properties, component, ComponentFactory.getInstance().getDescriptor(pd.getPropertyType()), componentKey, pk.alternative(), true);
        }
    }

    private void setValue(Properties properties, IComponent component, String componentKey, String propertyKey, ComponentDescriptor.PropertyDescriptor pd, boolean ignoreDefault, PropertyKey pk) {
        String value = null;
        if (pk != null && StringUtils.isNotEmpty(pk.alternative())) {
            value = properties.getProperty(pk.alternative());
        }
        if (value == null) {
            if (properties.containsKey(propertyKey)) {
                value = properties.getProperty(propertyKey);
            } else if (pd.getMethod().isAnnotationPresent(DefaultPropertyValue.class) && !ignoreDefault) {
                value = pd.getMethod().getAnnotation(DefaultPropertyValue.class).value();
            }
        }

        if (value != null) {
            ConfigHelper.setPropertyValue(component, componentKey, rtext.fromText(value, pd.getPropertyType()));
        } else {
            setMultiValues(properties, component, componentKey, propertyKey, pd, pk);
        }
    }

    private void setMultiValues(Properties properties, IComponent component, String componentKey, String propertyKey, ComponentDescriptor.PropertyDescriptor pd, PropertyKey pk) {
        Properties sp = ConfigHelper.extractProperties(properties, propertyKey + ".");
        if (pk != null && StringUtils.isNotEmpty(pk.alternative())) {
            Properties sp1 = ConfigHelper.extractProperties(properties, pk.alternative() + ".");
            if (pk.replaceAll()) {
                sp = sp1;
            } else {
                sp.putAll(sp1);
            }
        }

        if (sp != null && sp.size() > 0) {
            TypeToken<?> tt = TypeToken.of(pd.getPropertyType());
            if (tt.isArray()) {
                setArrayValue(tt, sp, component, componentKey);
            } else if (tt.isSubtypeOf(Collection.class)) {
                setCollectionValue(tt, sp, component, componentKey);
            } else if (tt.isSubtypeOf(Map.class)) {
                setMapValue(tt, sp, component, componentKey);
            }
        }
    }

    private void setArrayValue(TypeToken<?> tt, Properties sp, IComponent component, String componentKey) {
        ConfigHelper.setPropertyValue(component, componentKey, sp.stringPropertyNames().stream().sorted().map(k -> rtext.fromText(sp.getProperty(k), tt.getComponentType().getType()))
                .toArray(s -> (Object[]) Array.newInstance(tt.getComponentType().getRawType(), s)));
    }

    private void setCollectionValue(TypeToken<?> tt, Properties sp, IComponent component, String componentKey) {
        TypeToken<?> tt2 = tt.resolveType(tt.getRawType().getTypeParameters()[0]);
        Stream<Object> s = sp.stringPropertyNames().stream().sorted().map(k -> rtext.fromText(sp.getProperty(k), tt2.getType()));
        if (tt.isSubtypeOf(Set.class)) {
            ConfigHelper.setPropertyValue(component, componentKey, s.collect(Collectors.toSet()));
        } else if (tt.isSubtypeOf(List.class)) {
            ConfigHelper.setPropertyValue(component, componentKey, s.collect(Collectors.toList()));
        }
    }

    private void setMapValue(TypeToken<?> tt, Properties sp, IComponent component, String componentKey) {
        TypeToken<?> tt2 = tt.resolveType(tt.getRawType().getTypeParameters()[0]);
        TypeToken<?> tt3 = tt.resolveType(tt.getRawType().getTypeParameters()[1]);
        ConfigHelper.setPropertyValue(component, componentKey,
                sp.entrySet().stream().map(e -> new AbstractMap.SimpleEntry<>(rtext.fromText(e.getKey().toString(), tt2.getType()), rtext.fromText(e.getValue().toString(), tt3.getType())))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    public static class Builder {

        private String prefix;
        private IRtextConfiguration rtextConfiguration;

        /**
         * @param prefix prefix in properties
         */
        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        /**
         * @param rtextConfiguration Custom rtext with configuration
         */
        public Builder rtextConfiguration(IRtextConfiguration rtextConfiguration) {
            this.rtextConfiguration = rtextConfiguration;
            return this;
        }

        public AutoConfigProperty build() {
            AutoConfigProperty autoConfigProperty = new AutoConfigProperty();
            autoConfigProperty.prefix = prefix;
            autoConfigProperty.rtext = new Rtext(rtextConfiguration == null ? RtextConfigurationBuilder.newBuilder().build() : rtextConfiguration);
            return autoConfigProperty;
        }
    }
}
