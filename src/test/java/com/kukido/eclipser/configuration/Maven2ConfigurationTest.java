package com.kukido.eclipser.configuration;

import com.intellij.testFramework.LightIdeaTestCase;
import com.kukido.eclipser.EclipserException;

public class Maven2ConfigurationTest extends LightIdeaTestCase {

    public void testGetCommandWithMavenPluginDisabled() throws Exception {
        Maven2Configuration configuration = new Maven2Configuration(
                "kukido-test",
                true,
                new String[]{"local", "remote"},
                "clean compile",
                "/home/test/eclipser"
        );

        try {
            configuration.getCommand();
        } catch (EclipserException ee) {
            assertEquals("Maven plugin is installed, but not enabled. Please enable Maven plugin to continue.", ee.getMessage());
        }
    }
}
