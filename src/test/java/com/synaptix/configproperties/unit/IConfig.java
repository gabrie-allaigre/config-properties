package com.synaptix.configproperties.unit;

import com.synaptix.component.IComponent;
import com.synaptix.component.annotation.ComponentBean;

import java.nio.file.Path;

@ComponentBean
public interface IConfig extends IComponent {

    String getNomadeSerlvetUrl();

    ServiceImplType getServiceImplType();

    long getMaxSizeUploadAvarieImage();

    Path getPublicAttachmentsDirectory();

    int getNumber();

    enum ServiceImplType {
        NomadeServlet, Fake, RusService
    }

}
