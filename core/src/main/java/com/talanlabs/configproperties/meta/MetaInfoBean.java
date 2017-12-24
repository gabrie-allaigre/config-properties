package com.talanlabs.configproperties.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

public abstract class MetaInfoBean<E> {

    protected final Class<E> beanClass;

    protected MetaInfoBean(Class<E> beanClass) {
        super();

        this.beanClass = beanClass;
    }

    /**
     * @return get bean class
     */
    public final Class<E> getBeanClass() {
        return beanClass;
    }

    /**
     * @return create instance
     */
    public abstract E createInstance();

    /**
     * @return get all properties
     */
    public abstract Set<String> getPropertyNames();

    /**
     * Set a property value
     *
     * @param bean         bean to set
     * @param propertyName property
     * @param value        value to set
     */
    public abstract void setPropertyValue(E bean, String propertyName, Object value);

    /**
     * Get a property value
     *
     * @param bean         bean to get
     * @param propertyName property
     * @return value to get
     */
    public abstract Object getPropertyValue(E bean, String propertyName);

    /**
     * Get a property type
     *
     * @param propertyName property
     * @return type of property
     */
    public abstract Type getPropertyType(String propertyName);

    /**
     * Get a property class
     *
     * @param propertyName property
     * @return class of property
     */
    public abstract Class<?> getPropertyClass(String propertyName);

    /**
     * Verify if annotation is present for property
     *
     * @param propertyName    property
     * @param annotationClass annotation type
     * @return true if present
     */
    public abstract boolean isPropertyAnnotationPresent(String propertyName, Class<? extends Annotation> annotationClass);

    /**
     * Get annotation property
     *
     * @param propertyName    property
     * @param annotationClass annotation type
     * @return null if not present
     */
    public abstract <A extends Annotation> A getPropertyAnnotation(String propertyName, Class<A> annotationClass);

}
