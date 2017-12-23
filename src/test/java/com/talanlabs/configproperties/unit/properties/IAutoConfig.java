package com.talanlabs.configproperties.unit.properties;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;
import com.talanlabs.configproperties.properties.autoconfig.DefaultPropertyValue;
import com.talanlabs.configproperties.properties.autoconfig.PropertyKey;
import com.talanlabs.configproperties.properties.autoconfig.SubConfig;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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

    List<String> getGroups();

    String[] getRoles();

    Set<Type> getEnums();

    Properties getProperties();

    @PropertyKey(alternative = "GROUP")
    Map<String, String> getGroupMap();

    @PropertyKey(value = "groupMap", alternative = "GROUP", replaceAll = true)
    Map<String, String> getGroupMap2();

    Map<Integer, Type> getTypeMap();

    Map<Type, Boolean> getBooleanMap();

    enum Type {
        NomadeServlet, Fake, RusService
    }

    @ComponentBean
    @SubConfig
    interface ISubConfig extends IComponent {

        @PropertyKey(alternative = "HOST")
        String getSmtpHost();

        Integer getSmtpPort();

        @PropertyKey(alternative = "AUTH")
        IAuthConfig getAuth();
    }

    @SubConfig
    interface IAuthConfig extends IComponent {

        String getLogin();

        String getPassword();

        @DefaultPropertyValue("true")
        boolean isSsl();

        @DefaultPropertyValue("true")
        boolean isAuto();

    }
}
