package com.talanlabs.configproperties.component.unit.properties;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@ComponentBean
public interface IConfig extends IComponent {

    String getNomadeSerlvetUrl();

    ServiceImplType getServiceImplType();

    long getMaxSizeUploadAvarieImage();

    Path getPublicAttachmentsDirectory();

    int getNumber();

    IMailConfig getMailConfig();

    List<String> getGroups();

    String[] getRoles();

    Set<String> getEnums();

    Properties getProperties();

    Map<String, String> getGroupMap();

    Map<Integer, ServiceImplType> getTypeMap();

    Map<ServiceImplType, Boolean> getBooleanMap();

    enum ServiceImplType {
        NomadeServlet, Fake, RusService
    }

    @ComponentBean
    interface IMailConfig extends IComponent {

        String getSmptHost();

        Integer getSmptPort();

    }
}
