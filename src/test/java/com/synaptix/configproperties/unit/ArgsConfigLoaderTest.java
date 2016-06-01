package com.synaptix.configproperties.unit;

import com.synaptix.configproperties.loader.ArgsConfigLoader;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;

public class ArgsConfigLoaderTest {

    @Test
    public void testDefaultArgsConfigLoader() throws IOException {
        ArgsConfigLoader argsConfigLoader = new ArgsConfigLoader(() -> new String[]{"--server.mail.to=test@test.com", "--toto.to=", "non=vrai"});
        Assertions.assertThat(argsConfigLoader.readProperties()).isNotNull().containsEntry("server.mail.to", "test@test.com").containsEntry("toto.to", "").doesNotContainKey("non").hasSize(2);
    }

    @Test
    public void testNullArgsConfigLoader() throws IOException {
        ArgsConfigLoader argsConfigLoader = new ArgsConfigLoader(null);
        Assertions.assertThat(argsConfigLoader.readProperties()).isNull();
    }

    @Test
    public void testStaticArgsConfigLoader() throws IOException {
        ArgsConfigLoader.StaticGetArgs.getINSTANCE().setArgs(new String[]{"--server.mail.to=test@test.com"});

        ArgsConfigLoader argsConfigLoader = new ArgsConfigLoader();
        Assertions.assertThat(argsConfigLoader.readProperties()).isNotNull().containsEntry("server.mail.to", "test@test.com");
    }
}
