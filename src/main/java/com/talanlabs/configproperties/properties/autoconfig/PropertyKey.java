package com.talanlabs.configproperties.properties.autoconfig;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Inherited
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
