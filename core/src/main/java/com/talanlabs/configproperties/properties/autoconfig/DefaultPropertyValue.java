package com.talanlabs.configproperties.properties.autoconfig;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Inherited
public @interface DefaultPropertyValue {

    String value();

}
