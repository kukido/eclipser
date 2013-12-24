package com.kukido.eclipser.command;

import com.intellij.execution.RunManager;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.LightIdeaTestCase;
import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.configuration.JavaConfiguration;

public class AddApplicationConfigurationCommandTest extends LightIdeaTestCase {

    public static final String TEST_CASE_MODULE_NAME = "light_idea_test_case";

    private AddApplicationConfigurationCommand command;

    public void testExecuteWithFullConfiguration() throws Exception {
        JavaConfiguration configuration = new JavaConfiguration(
                "kukido-test",
                "com.kukido.example.MainClass",
                TEST_CASE_MODULE_NAME,
                "-ea -Xmx256M",
                "--port 8080"
        );

        command = new AddApplicationConfigurationCommand(configuration);

        Project project = getProject();
        command.execute(project);

        validateCreatedConfiguration(configuration);
    }

    public void testExecuteWithUnknownModule() throws Exception {
        JavaConfiguration configuration = new JavaConfiguration(
                "configuration",
                "Main",
                "undefined",
                null,
                null
        );
        command = new AddApplicationConfigurationCommand(configuration);

        try {
            command.execute(getProject());
            fail("Execute should throw an exception when module is not found");
        } catch (EclipserException ignored) {}
    }

    public void testExecuteWithMinimalConfiguration() throws Exception {
        JavaConfiguration configuration = new JavaConfiguration(
                "configuration",
                "Main",
                TEST_CASE_MODULE_NAME,
                null,
                null
        );
        command = new AddApplicationConfigurationCommand(configuration);
        command.execute(getProject());

        validateCreatedConfiguration(configuration);
    }

	public void testExecuteWithExistingConfiguration() throws Exception {
		JavaConfiguration configuration = new JavaConfiguration(
				"configuration",
				"Main",
				TEST_CASE_MODULE_NAME,
				null,
				null
		);
		command = new AddApplicationConfigurationCommand(configuration);
		command.execute(getProject());
		try {
			command.execute(getProject());
			fail("Execute should throw an exception on duplicate configuration");
		} catch (EclipserException ignored) {}
	}

    private void validateCreatedConfiguration(JavaConfiguration configuration) {
        ApplicationConfiguration applicationConfiguration = null;
        RunManager manager = RunManager.getInstance(getProject());
        // todo: java: getAllConfigurations() in com.intellij.execution.RunManager has been deprecated
        RunConfiguration[] configurations = manager.getAllConfigurations();
        for (RunConfiguration runConfiguration : configurations) {
            if (runConfiguration.getName().equals(configuration.getConfigurationName())) {
                if (runConfiguration instanceof ApplicationConfiguration) {
                    applicationConfiguration = (ApplicationConfiguration)runConfiguration;
                }
            }
        }

        if (applicationConfiguration == null) {
            fail("Application configuration was not created");
        } else {
            assertEquals(configuration.getConfigurationName(), applicationConfiguration.getName());
            assertEquals(configuration.getVmParameters(), applicationConfiguration.getVMParameters());
            assertEquals(configuration.getProgramParameters(), applicationConfiguration.getProgramParameters());
            assertEquals(configuration.getMainClassName(), applicationConfiguration.getRunClass());
            assertEquals(configuration.getModuleName(), applicationConfiguration.getConfigurationModule().getModuleName());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        RunManagerImpl manager = (RunManagerImpl) RunManager.getInstance(getProject());
        manager.clearAll();
        super.tearDown();
    }
}
