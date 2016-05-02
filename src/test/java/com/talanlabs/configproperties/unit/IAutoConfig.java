package com.talanlabs.configproperties.unit;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;
import com.talanlabs.configproperties.properties.autoconfig.DefaultPropertyValue;
import com.talanlabs.configproperties.properties.autoconfig.PropertyKey;

@ComponentBean
public interface IAutoConfig extends IComponent {

    String getName();

    Type getType();

    long getSize();

    @DefaultPropertyValue("51")
    int getWidth();

    @PropertyKey("toto")
    int getTiti();

    ISubConfig getSubConfig();

    enum Type {
        NomadeServlet, Fake, RusService
    }

    @ComponentBean
    interface ISubConfig extends IComponent {

        String getSmtpHost();

        Integer getSmtpPort();

    }
}
