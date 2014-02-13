package com.kukido.eclipser.command;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.LightIdeaTestCase;
import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.configuration.Maven2Configuration;
import org.jetbrains.idea.maven.execution.MavenRunConfiguration;
import org.jetbrains.idea.maven.execution.MavenRunnerParameters;

import java.util.List;

public class AddMaven2ConfigurationCommandTest extends LightIdeaTestCase {

    private AddMaven2ConfigurationCommand command;

    public void testExecuteWithFullConfiguration() throws Exception {
        Maven2Configuration configuration = new Maven2Configuration(
                "kukido-test",
                true,
                new String[]{"local", "remote"},
                "clean compile",
                "/home/eclipser"
        );

        configuration.getCommand();

        command = new AddMaven2ConfigurationCommand(configuration);

        Project project = getProject();
        command.execute(project);

        validateCreatedConfiguration(configuration);
    }

    public void testExecuteWithDuplicateName() throws Exception {
        Maven2Configuration configuration = new Maven2Configuration(
                "kukido-test",
                true,
                new String[]{"local", "remote"},
                "clean compile",
                "/home/eclipser"
        );

        configuration.getCommand();

        command = new AddMaven2ConfigurationCommand(configuration);

        Project project = getProject();
        command.execute(project);

        try {
            command.execute(getProject());
            fail("Execute should throw an exception on duplicate configuration");
        } catch (EclipserException ignored) {
        }
    }

    private void validateCreatedConfiguration(Maven2Configuration configuration) {
        MavenRunConfiguration mavenRunConfiguration = null;
        RunManager manager = RunManager.getInstance(getProject());
        List<RunConfiguration> configurations = manager.getAllConfigurationsList();
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
            MavenRunnerParameters parameters = mavenRunConfiguration.getRunnerParameters();
            assertEquals(configuration.isResolveToWorkspace(), parameters.isResolveToWorkspace());
            assertContainsOrdered(parameters.getGoals(), configuration.getGoals());
            assertContainsElements(parameters.getProfilesMap().entrySet(), configuration.getProfilesMap().entrySet());
            assertEquals(configuration.getWorkingDirectory(), parameters.getWorkingDirPath());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        RunManagerImpl manager = (RunManagerImpl) RunManager.getInstance(getProject());
        manager.clearAll();
        super.tearDown();
    }
}
