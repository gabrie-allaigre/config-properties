package com.talanlabs.configproperties.properties.autoconfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PropertyKey {

    /**
     * @return Change name
     */
    String value() default "";

    /**
     * @return Change full key
     */
    String alternative() default "";

    /**
     * @return AutoConfig ignore field
     */
    boolean ignore() default false;

    /**
     * @return if true, alternative replace all value
     */
    boolean replaceAll() default false;
}
