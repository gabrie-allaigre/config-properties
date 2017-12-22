package com.talanlabs.configproperties.unit;

import com.talanlabs.configproperties.utils.ConfigHelper;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;

import java.util.Properties;

public class ConfigHelperTest {

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
