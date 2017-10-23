package com.talanlabs.configproperties.unit;

import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.configproperties.unit.properties.IConfig;
import com.talanlabs.configproperties.utils.ConfigHelper;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;

import java.util.Properties;

public class ConfigHelperTest {

    @Test
    public void testSetPropertyValue() {
        IConfig config = ComponentFactory.getInstance().createInstance(IConfig.class);
        ConfigHelper.setPropertyValue(config, "number", 10);
        ConfigHelper.setPropertyValue(config, "roles", new String[] { "admin", "user" });
        ConfigHelper.setPropertyValue(config, "mailConfig.smptHost", "rien.com");

        BDDAssertions.then(config.getNumber()).isEqualTo(10);
        BDDAssertions.then(config.getRoles()).containsExactly("admin", "user");
        BDDAssertions.then(config.getMailConfig().getSmptHost()).isEqualTo("rien.com");
    }

    @Test
    public void testExtractProperty() {
        Properties p1 = new Properties();
        p1.setProperty("server.name","Gabriel");
        p1.setProperty("server.email","Gabriel@toto.com");
        p1.setProperty("toto","tata");
        p1.setProperty("name","rien");

        Properties p2 = ConfigHelper.extractProperties(p1,"server.");
        BDDAssertions.then(p2.size()).isEqualTo(2);
        BDDAssertions.then(p2).containsEntry("name","Gabriel").containsEntry("email","Gabriel@toto.com");
    }
}
