package com.talanlabs.configproperties.unit.properties;

import com.talanlabs.configproperties.properties.autoconfig.SubConfig;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ConfigBean {

    private String nomadeSerlvetUrl;
    private ServiceImplType serviceImplType;
    private long maxSizeUploadAvarieImage;
    private Path publicAttachmentsDirectory;
    private int number;
    private MailConfigBean mailConfig;
    private List<String> groups;
    private String[] roles;
    private Set<String> enums;
    private Properties properties;
    private Map<String, String> groupMap;
    private Map<Integer, ServiceImplType> typeMap;
    private Map<ServiceImplType, Boolean> booleanMap;

    public String getNomadeSerlvetUrl() {
        return nomadeSerlvetUrl;
    }

    public ServiceImplType getServiceImplType() {
        return serviceImplType;
    }

    public long getMaxSizeUploadAvarieImage() {
        return maxSizeUploadAvarieImage;
    }

    public Path getPublicAttachmentsDirectory() {
        return publicAttachmentsDirectory;
    }

    public int getNumber() {
        return number;
    }

    public MailConfigBean getMailConfig() {
        return mailConfig;
    }

    public List<String> getGroups() {
        return groups;
    }

    public String[] getRoles() {
        return roles;
    }

    public Set<String> getEnums() {
        return enums;
    }

    public Properties getProperties() {
        return properties;
    }

    public Map<String, String> getGroupMap() {
        return groupMap;
    }

    public Map<Integer, ServiceImplType> getTypeMap() {
        return typeMap;
    }

    public Map<ServiceImplType, Boolean> getBooleanMap() {
        return booleanMap;
    }

    public enum ServiceImplType {
        NomadeServlet, Fake, RusService
    }

    @SubConfig
    public static class MailConfigBean {

        private String smptHost;
        private Integer smptPort;

        public String getSmptHost() {
            return smptHost;
        }

        public Integer getSmptPort() {
            return smptPort;
        }
    }
}
