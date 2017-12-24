package com.talanlabs.configproperties.unit.properties;

import com.talanlabs.configproperties.properties.autoconfig.DefaultPropertyValue;
import com.talanlabs.configproperties.properties.autoconfig.PropertyKey;
import com.talanlabs.configproperties.properties.autoconfig.SubConfig;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class AutoConfigBean  {

    private String name;
    private Type type;
    private long size;
    @DefaultPropertyValue("51")
    private int width;
    @PropertyKey("toto")
    private int titi;
    private SubConfigBean subConfig;
    private List<String> groups;
    private String[] roles;
    private Set<Type> enums;
    private Properties properties;
    @PropertyKey(alternative = "GROUP")
    private Map<String, String> groupMap;
    @PropertyKey(value = "groupMap", alternative = "GROUP", replaceAll = true)
    private Map<String, String> groupMap2;
    private Map<Integer, Type> typeMap;
    private Map<Type, Boolean> booleanMap;

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public long getSize() {
        return size;
    }

    public int getWidth() {
        return width;
    }

    public int getTiti() {
        return titi;
    }

    public SubConfigBean getSubConfig() {
        return subConfig;
    }

    public List<String> getGroups() {
        return groups;
    }

    public String[] getRoles() {
        return roles;
    }

    public Set<Type> getEnums() {
        return enums;
    }

    public Properties getProperties() {
        return properties;
    }

    public Map<String, String> getGroupMap() {
        return groupMap;
    }

    public Map<String, String> getGroupMap2() {
        return groupMap2;
    }

    public Map<Integer, Type> getTypeMap() {
        return typeMap;
    }

    public Map<Type, Boolean> getBooleanMap() {
        return booleanMap;
    }

    public enum Type {
        NomadeServlet, Fake, RusService
    }

    @SubConfig
    public static class SubConfigBean {

        @PropertyKey(alternative = "HOST")
        private String smtpHost;
        private Integer smtpPort;
        @PropertyKey(alternative = "AUTH")
        private AuthConfigBean auth;

        public String getSmtpHost() {
            return smtpHost;
        }

        public Integer getSmtpPort() {
            return smtpPort;
        }

        public AuthConfigBean getAuth() {
            return auth;
        }
    }

    @SubConfig
    public static class AuthConfigBean {

        private String login;
        private String password;
        @DefaultPropertyValue("true")
        private boolean ssl;
        @DefaultPropertyValue("true")
        private boolean auto;

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public boolean isSsl() {
            return ssl;
        }

        public boolean isAuto() {
            return auto;
        }
    }
}
