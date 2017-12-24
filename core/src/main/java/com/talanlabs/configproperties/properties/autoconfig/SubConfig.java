package com.talanlabs.configproperties.properties.autoconfig;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface SubConfig {

}
