package com.kukido.eclipser.command;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.LightIdeaTestCase;
import com.kukido.eclipser.configuration.Maven2Configuration;
import org.jetbrains.idea.maven.execution.MavenRunConfiguration;

public class AddMaven2ConfigurationCommandTest extends LightIdeaTestCase {

    private AddMaven2ConfigurationCommand command;

    /*
    public void testExecuteWithFullConfiguration() throws Exception {
        Maven2Configuration configuration = new Maven2Configuration(
                "kukido-test",
                true,
                new String[]{"local", "remote"},
                "clean compile"
        );

        configuration.getCommand();

        command = new AddMaven2ConfigurationCommand(configuration);

        Project project = getProject();
        command.execute(project);

        validateCreatedConfiguration(configuration);
    }
    */

    private void validateCreatedConfiguration(Maven2Configuration configuration) {
        MavenRunConfiguration mavenRunConfiguration = null;
        RunManager manager = RunManager.getInstance(getProject());
        // todo: java: getAllConfigurations() in com.intellij.execution.RunManager has been deprecated
        RunConfiguration[] configurations = manager.getAllConfigurations();
        for (RunConfiguration runConfiguration : configurations) {
            if (runConfiguration.getName().equals(configuration.getConfigurationName())) {
                if (runConfiguration instanceof MavenRunConfiguration) {
                    mavenRunConfiguration = (MavenRunConfiguration)runConfiguration;
                }
            }
        }

        if (mavenRunConfiguration == null) {
            fail("Maven run configuration was not created");
        } else {
            assertEquals(configuration.getConfigurationName(), mavenRunConfiguration.getName());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        RunManagerImpl manager = (RunManagerImpl) RunManager.getInstance(getProject());
        manager.clearAll();
        super.tearDown();
    }
}
