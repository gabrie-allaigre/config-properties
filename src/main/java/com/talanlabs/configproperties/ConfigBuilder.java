package com.talanlabs.configproperties;

import com.talanlabs.configproperties.loader.IConfigLoader;
import com.talanlabs.configproperties.meta.DefaultMetaInfoBean;
import com.talanlabs.configproperties.meta.MetaBeanException;
import com.talanlabs.configproperties.meta.MetaInfoBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class ConfigBuilder<E> {

    private static final Logger LOG = LogManager.getLogger(ConfigBuilder.class);

    private final Function<Class<?>, MetaInfoBean> createMetaInfoBeanFunction;
    private final MetaInfoBean<E> metaInfoBean;
    private final List<IConfigProperty> configProperties;

    private IConfigLoader configLoader;

    @SuppressWarnings("unchecked")
    private ConfigBuilder(Class<E> beanClass, Function<Class<?>, MetaInfoBean> createMetaInfoBeanFunction) {
        super();

        this.createMetaInfoBeanFunction = createMetaInfoBeanFunction;
        this.metaInfoBean = createMetaInfoBeanFunction.apply(beanClass);
        this.configProperties = new ArrayList<>();
    }

    /**
     * Create config bean
     *
     * @param beanClass bean class
     */
    public static <E> ConfigBuilder<E> newBuilder(Class<E> beanClass) {
        return new ConfigBuilder<>(beanClass, DefaultMetaInfoBean::new);
    }

    /**
     * Create a config bean
     *
     * @param beanClass                  bean class
     * @param createMetaInfoBeanFunction create metainfobean
     * @return
     */
    public static <E> ConfigBuilder<E> newBuilder(Class<E> beanClass, Function<Class<?>, MetaInfoBean> createMetaInfoBeanFunction) {
        return new ConfigBuilder<>(beanClass, createMetaInfoBeanFunction);
    }

    /**
     * @param configLoader change config loader, default is DefaultConfigLoader
     */
    public ConfigBuilder<E> configLoader(IConfigLoader configLoader) {
        this.configLoader = configLoader;
        return this;
    }

    /**
     * Add config property
     */
    public ConfigBuilder<E> configProperty(IConfigProperty configProperty, IConfigProperty... configProperties) {
        this.configProperties.add(configProperty);
        if (configProperties != null) {
            this.configProperties.addAll(Arrays.asList(configProperties));
        }
        return this;
    }

    /**
     * @return New config component
     */
    public E build() {
        if (configLoader == null) {
            configLoader = IConfigLoader.compose(IConfigLoader.firstNotNull(IConfigLoader.system("config.file"), IConfigLoader.resource("config.properties"), IConfigLoader.resource("config.yml")),
                    IConfigLoader.systemProperties(), IConfigLoader.envProperties());
        }

        Properties properties = configLoader.readProperties();

        E config = this.metaInfoBean.createInstance();

        IConfigProperty.Context context = new ContextImpl(config);

        configProperties.forEach(configProperty -> configProperty.setProperty(context, properties));
        return config;
    }

    private class ContextImpl implements IConfigProperty.Context<E> {

        private final E config;
        private final com.google.common.cache.Cache<Class<?>, MetaInfoBean<?>> metaInfoBeanCache;

        ContextImpl(E config) {
            super();

            this.config = config;

            this.metaInfoBeanCache = com.google.common.cache.CacheBuilder.newBuilder().concurrencyLevel(4).build();
        }

        @Override
        public void setPropertyValue(String propertyName, Object value) {
            LOG.info(String.format("Set property %1s -> %2s", propertyName, value));
            setPropertyValue(metaInfoBean, config, propertyName, value);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <F> MetaInfoBean<F> forBeanClass(Class<F> beanClass) {
            try {
                return (MetaInfoBean<F>) this.metaInfoBeanCache.get(beanClass, () -> createMetaInfoBeanFunction.apply(beanClass));
            } catch (ExecutionException e) {
                throw new MetaBeanException("Failed to get MetaInfoBean for Bean=" + beanClass, e);
            }
        }

        @Override
        public MetaInfoBean<E> getMetaInfoBean() {
            return metaInfoBean;
        }

        @SuppressWarnings("unchecked")
        private <F> void setPropertyValue(MetaInfoBean<F> metaInfoBean, F bean, String propertyName, Object value) {
            if (bean == null) {
                return;
            }
            int index = propertyName.indexOf('.');
            if (index == -1) {
                metaInfoBean.setPropertyValue(bean, propertyName, value);
            } else {
                String head = propertyName.substring(0, index);

                MetaInfoBean<Object> subMetaInfoBean = this.forBeanClass((Class<Object>) metaInfoBean.getPropertyClass(head));
                Object subBean = metaInfoBean.getPropertyValue(bean, head);
                if (subBean == null) {
                    subBean = subMetaInfoBean.createInstance();

                    metaInfoBean.setPropertyValue(bean, head, subBean);
                }
                setPropertyValue(subMetaInfoBean, subBean, propertyName.substring(index + 1), value);
            }
        }
    }
}
