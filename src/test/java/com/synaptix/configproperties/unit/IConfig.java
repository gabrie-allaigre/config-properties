package com.synaptix.configproperties.unit;

import com.synaptix.component.IComponent;
import com.synaptix.component.annotation.ComponentBean;

import java.nio.file.Path;
import java.util.List;
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

    enum ServiceImplType {
        NomadeServlet, Fake, RusService
    }

    @ComponentBean
    public interface IMailConfig extends IComponent {

        String getSmptHost();

        Integer getSmptPort();

    }
}
