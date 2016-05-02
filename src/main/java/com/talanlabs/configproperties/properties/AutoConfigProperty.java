package com.talanlabs.configproperties.properties;

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

import java.util.Properties;

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
        setProperty(properties, component, cd, "");
        return component;
    }

    private void setProperty(Properties properties, IComponent component, ComponentDescriptor<?> cd, String prefix) {
        cd.getPropertyDescriptors().forEach(pd -> set(properties, component, prefix, pd));
    }

    private void set(Properties properties, IComponent component, String previous, ComponentDescriptor.PropertyDescriptor pd) {
        String realKey = (StringUtils.isNotBlank(previous) ? previous + "." : "") + pd.getPropertyName();

        if (ComponentFactory.getInstance().isComponentType(pd.getPropertyType())) {
            setProperty(properties, component, ComponentFactory.getInstance().getDescriptor(pd.getPropertyType()), realKey);
        } else {
            String key = (StringUtils.isNotBlank(previous) ? previous + "." : "");
            if (pd.getMethod().isAnnotationPresent(PropertyKey.class)) {
                key += pd.getMethod().getAnnotation(PropertyKey.class).value();
            } else {
                key += pd.getPropertyName();
            }

            String fullKey = (StringUtils.isNotBlank(prefix) ? prefix + "." : "") + key;
            if (properties.containsKey(fullKey)) {
                String value = properties.getProperty(fullKey);
                ConfigHelper.setPropertyValue(component, realKey, rtext.fromText(value, pd.getPropertyType()));
            } else if (pd.getMethod().isAnnotationPresent(DefaultPropertyValue.class)) {
                String value = pd.getMethod().getAnnotation(DefaultPropertyValue.class).value();
                ConfigHelper.setPropertyValue(component, realKey, rtext.fromText(value, pd.getPropertyType()));
            }
        }
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
