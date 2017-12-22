package com.talanlabs.configproperties.meta;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.factory.ComponentFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.function.Function;

public class MetaInfoBeanDelegate<E> extends MetaInfoBean<E> {

    private MetaInfoBean<E> delegate;

    public MetaInfoBeanDelegate(Class<E> beanClass) {
        this(beanClass, c -> {
            if (ComponentFactory.getInstance().isComponentType(beanClass)) {
                return (MetaInfoBean<E>) new ComponentMetaInfoBean<>((Class<IComponent>) beanClass);
            } else {
                return new DefaultMetaInfoBean<>(beanClass);
            }
        });
    }

    public MetaInfoBeanDelegate(Class<E> beanClass, Function<Class<E>, MetaInfoBean<E>> function) {
        super(beanClass);

        this.delegate = function.apply(beanClass);
    }

    @Override
    public E createInstance() {
        return this.delegate.createInstance();
    }

    @Override
    public Set<String> getPropertyNames() {
        return this.delegate.getPropertyNames();
    }

    @Override
    public void setPropertyValue(E bean, String propertyName, Object value) {
        this.delegate.setPropertyValue(bean, propertyName, value);
    }

    @Override
    public Object getPropertyValue(E bean, String propertyName) {
        return this.delegate.getPropertyValue(bean, propertyName);
    }

    @Override
    public Type getPropertyType(String propertyName) {
        return this.delegate.getPropertyType(propertyName);
    }

    @Override
    public Class<?> getPropertyClass(String propertyName) {
        return this.delegate.getPropertyClass(propertyName);
    }

    @Override
    public boolean isPropertyAnnotationPresent(String propertyName, Class<? extends Annotation> annotationClass) {
        return this.delegate.isPropertyAnnotationPresent(propertyName, annotationClass);
    }

    @Override
    public <A extends Annotation> A getPropertyAnnotation(String propertyName, Class<A> annotationClass) {
        return this.delegate.getPropertyAnnotation(propertyName, annotationClass);
    }
}
