package com.talanlabs.configproperties.unit.yaml;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;
import com.talanlabs.configproperties.properties.autoconfig.DefaultPropertyValue;
import com.talanlabs.configproperties.properties.autoconfig.PropertyKey;

@ComponentBean
public interface IConfig1 extends IComponent {

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

    interface ISubConfig extends IComponent {

        String getSmtpHost();

        Integer getSmtpPort();

        IAuthConfig getAuth();
    }

    interface IAuthConfig extends IComponent {

        String getLogin();

        String getPassword();

    }
}
