package com.talanlabs.configproperties.component.meta;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.factory.ComponentDescriptor;
import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.configproperties.meta.DefaultMetaInfoBean;
import com.talanlabs.configproperties.meta.MetaInfoBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.function.Function;

public class ComponentMetaInfoBean<E extends IComponent> extends MetaInfoBean<E> {

    private ComponentDescriptor<E> componentDescriptor;

    public ComponentMetaInfoBean(Class<E> beanClass) {
        super(beanClass);

        this.componentDescriptor = ComponentFactory.getInstance().getDescriptor(beanClass);
    }

    public static <F> Function<Class<?>, MetaInfoBean> createDelegateMetaInfoBeanFunction() {
        return beanClass -> {
            if (ComponentFactory.getInstance().isComponentType(beanClass)) {
                return (MetaInfoBean<F>) new ComponentMetaInfoBean<>((Class<IComponent>) beanClass);
            } else {
                return new DefaultMetaInfoBean<>(beanClass);
            }
        };
    }

    @Override
    public E createInstance() {
        return ComponentFactory.getInstance().createInstance(beanClass);
    }

    @Override
    public Set<String> getPropertyNames() {
        return this.componentDescriptor.getPropertyNames();
    }

    private void checkProperty(String propertyName) {
        if (!this.componentDescriptor.getPropertyNames().contains(propertyName)) {
            throw new IllegalArgumentException("Property " + propertyName + " not found in " + beanClass);
        }
    }

    @Override
    public void setPropertyValue(E bean, String propertyName, Object value) {
        checkProperty(propertyName);
        bean.straightSetProperty(propertyName, value);
    }

    @Override
    public Object getPropertyValue(E bean, String propertyName) {
        checkProperty(propertyName);
        return bean.straightGetProperty(propertyName);
    }

    @Override
    public Type getPropertyType(String propertyName) {
        checkProperty(propertyName);
        return componentDescriptor.getPropertyType(propertyName);
    }

    @Override
    public Class<?> getPropertyClass(String propertyName) {
        checkProperty(propertyName);
        return componentDescriptor.getPropertyClass(propertyName);
    }

    @Override
    public boolean isPropertyAnnotationPresent(String propertyName, Class<? extends Annotation> annotationClass) {
        checkProperty(propertyName);
        return componentDescriptor.getPropertyDescriptor(propertyName).getMethod().isAnnotationPresent(annotationClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A extends Annotation> A getPropertyAnnotation(String propertyName, Class<A> annotationClass) {
        checkProperty(propertyName);
        return componentDescriptor.getPropertyDescriptor(propertyName).getMethod().getAnnotation(annotationClass);
    }
}
