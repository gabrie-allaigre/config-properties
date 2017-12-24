package com.talanlabs.configproperties.properties;

import com.google.common.reflect.TypeToken;
import com.talanlabs.configproperties.IConfigProperty;
import com.talanlabs.configproperties.meta.MetaInfoBean;
import com.talanlabs.configproperties.properties.autoconfig.DefaultPropertyValue;
import com.talanlabs.configproperties.properties.autoconfig.PropertyKey;
import com.talanlabs.configproperties.properties.autoconfig.SubConfig;
import com.talanlabs.configproperties.utils.ConfigHelper;
import com.talanlabs.rtext.Rtext;
import com.talanlabs.rtext.configuration.IRtextConfiguration;
import com.talanlabs.rtext.configuration.RtextConfigurationBuilder;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoConfigProperty implements IConfigProperty {

    private String prefix;
    private Rtext rtext;

    private AutoConfigProperty() {
        super();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void setProperty(Context<?> context, Properties properties) {
        setProperty(context, properties, context.getMetaInfoBean(), "", prefix, false);
    }

    private void setProperty(Context<?> context, Properties properties, MetaInfoBean<?> metaInfoBean, String beanPrefix, String propertyPrefix, boolean ignoreDefault) {
        metaInfoBean.getPropertyNames().forEach(propertyName -> set(context, properties, beanPrefix, propertyPrefix, metaInfoBean, propertyName, ignoreDefault));
    }

    private void set(Context<?> context, Properties properties, String beanPrefix, String propertyPrefix, MetaInfoBean<?> metaInfoBean, String propertyName, boolean ignoreDefault) {
        String beanKey = (StringUtils.isNotBlank(beanPrefix) ? beanPrefix + "." : "") + propertyName;

        PropertyKey pk = metaInfoBean.getPropertyAnnotation(propertyName, PropertyKey.class);
        if (pk != null && pk.ignore()) {
            return;
        }

        String propertyKey = (StringUtils.isNotBlank(propertyPrefix) ? propertyPrefix + "." : "");
        if (pk != null && StringUtils.isNotEmpty(pk.value())) {
            propertyKey += pk.value();
        } else {
            propertyKey += propertyName;
        }

        Class<?> propertyClass = metaInfoBean.getPropertyClass(propertyName);
        if (Properties.class.isAssignableFrom(propertyClass)) {
            context.setPropertyValue(beanKey, ConfigHelper.extractProperties(properties, StringUtils.isNotBlank(propertyPrefix) ? propertyPrefix : ""));
        } else {
            boolean res = setValue(context, properties, beanKey, propertyKey, metaInfoBean, propertyName, ignoreDefault, pk);
            if (!res && propertyClass.isAnnotationPresent(SubConfig.class)) {
                setBean(context, properties, beanKey, propertyKey, metaInfoBean, propertyName, ignoreDefault, pk);
            }
        }
    }

    private void setBean(Context<?> context, Properties properties, String beanKey, String propertyKey, MetaInfoBean<?> metaInfoBean, String propertyName, boolean ignoreDefault, PropertyKey pk) {
        setProperty(context, properties, context.forBeanClass(metaInfoBean.getPropertyClass(propertyName)), beanKey, propertyKey, ignoreDefault);

        if (pk != null && StringUtils.isNotEmpty(pk.alternative())) {
            setProperty(context, properties, context.forBeanClass(metaInfoBean.getPropertyClass(propertyName)), beanKey, pk.alternative(), true);
        }
    }

    private boolean setValue(Context<?> context, Properties properties, String beanKey, String propertyKey, MetaInfoBean<?> metaInfoBean, String propertyName, boolean ignoreDefault, PropertyKey pk) {
        String value = null;
        if (pk != null && StringUtils.isNotEmpty(pk.alternative())) {
            value = properties.getProperty(pk.alternative());
        }
        if (value == null) {
            if (properties.containsKey(propertyKey)) {
                value = properties.getProperty(propertyKey);
            } else if (metaInfoBean.isPropertyAnnotationPresent(propertyName, DefaultPropertyValue.class) && !ignoreDefault) {
                value = metaInfoBean.getPropertyAnnotation(propertyName, DefaultPropertyValue.class).value();
            }
        }

        if (value != null) {
            context.setPropertyValue(beanKey, rtext.fromText(value, metaInfoBean.getPropertyType(propertyName)));
            return true;
        } else {
            return setMultiValues(context, properties, beanKey, propertyKey, metaInfoBean, propertyName, pk);
        }
    }

    private boolean setMultiValues(Context<?> context, Properties properties, String beanKey, String propertyKey, MetaInfoBean<?> metaInfoBean, String propertyName, PropertyKey pk) {
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
            TypeToken<?> tt = TypeToken.of(metaInfoBean.getPropertyType(propertyName));
            if (tt.isArray()) {
                setArrayValue(context, tt, sp, beanKey);
                return true;
            } else if (tt.isSubtypeOf(Collection.class)) {
                setCollectionValue(context, tt, sp, beanKey);
                return true;
            } else if (tt.isSubtypeOf(Map.class)) {
                setMapValue(context, tt, sp, beanKey);
                return true;
            }
        }
        return false;
    }

    private void setArrayValue(Context<?> context, TypeToken<?> tt, Properties properties, String beanKey) {
        context.setPropertyValue(beanKey, properties.stringPropertyNames().stream().sorted().map(k -> rtext.fromText(properties.getProperty(k), tt.getComponentType().getType()))
                .toArray(s -> (Object[]) Array.newInstance(tt.getComponentType().getRawType(), s)));
    }

    private void setCollectionValue(Context<?> context, TypeToken<?> tt, Properties properties, String beanKey) {
        TypeToken<?> tt2 = tt.resolveType(tt.getRawType().getTypeParameters()[0]);
        Stream<Object> s = properties.stringPropertyNames().stream().sorted().map(k -> rtext.fromText(properties.getProperty(k), tt2.getType()));
        if (tt.isSubtypeOf(Set.class)) {
            context.setPropertyValue(beanKey, s.collect(Collectors.toSet()));
        } else if (tt.isSubtypeOf(List.class)) {
            context.setPropertyValue(beanKey, s.collect(Collectors.toList()));
        }
    }

    private void setMapValue(Context<?> context, TypeToken<?> tt, Properties properties, String beanKey) {
        TypeToken<?> tt2 = tt.resolveType(tt.getRawType().getTypeParameters()[0]);
        TypeToken<?> tt3 = tt.resolveType(tt.getRawType().getTypeParameters()[1]);
        context.setPropertyValue(beanKey,
                properties.entrySet().stream().map(e -> new AbstractMap.SimpleEntry<>(rtext.fromText(e.getKey().toString(), tt2.getType()), rtext.fromText(e.getValue().toString(), tt3.getType())))
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
