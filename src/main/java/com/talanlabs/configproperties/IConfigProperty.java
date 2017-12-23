package com.talanlabs.configproperties;

import com.talanlabs.configproperties.meta.MetaInfoBean;

import java.util.Properties;

public interface IConfigProperty {

    /**
     * Set propertie in bean
     *
     * @param context    context
     * @param properties current properties
     */
    void setProperty(Context<?> context, Properties properties);

    interface Context<E> {

        /**
         * Set value in bean, create sub bean
         *
         * @param propertyName property with dot
         * @param value        value of property
         */
        void setPropertyValue(String propertyName, Object value);

        MetaInfoBean<E> getMetaInfoBean();

        <F> MetaInfoBean<F> forBeanClass(Class<F> beanClass);
    }
}
