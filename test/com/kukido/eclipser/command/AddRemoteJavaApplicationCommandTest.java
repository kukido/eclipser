package com.kukido.eclipser.command;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.execution.remote.RemoteConfiguration;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.LightIdeaTestCase;
import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.configuration.RemoteJavaApplicationConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddRemoteJavaApplicationCommandTest extends LightIdeaTestCase {

    private static final String TEST_CASE_MODULE_NAME = "light_idea_test_case";

    private AddRemoteJavaApplicationCommand command;

    public void testExecute() throws Exception {

        Map<String, String> connectMap = new HashMap<String, String>(2);
        connectMap.put(RemoteJavaApplicationConfiguration.HOST_NAME_KEY, "hostname");
        connectMap.put(RemoteJavaApplicationConfiguration.PORT_KEY, "9000");

        @SuppressWarnings("ConstantConditions")
        RemoteJavaApplicationConfiguration configuration = new RemoteJavaApplicationConfiguration("test-execute", connectMap, AddRemoteJavaApplicationCommand.SOCKET_ATTACH_CONNECTOR, TEST_CASE_MODULE_NAME);

        command = (AddRemoteJavaApplicationCommand) configuration.getCommand();

        Project project = getProject();

        command.execute(project);


        RemoteConfiguration remoteConfiguration = null;
        RunManagerImpl manager = (RunManagerImpl) RunManager.getInstance(getProject());

        List<RunConfiguration> configurations = manager.getAllConfigurationsList();
        for (RunConfiguration runConfiguration : configurations) {
            if (runConfiguration.getName().equals(configuration.getName())) {
                if (runConfiguration instanceof RemoteConfiguration) {
                    remoteConfiguration = (RemoteConfiguration) runConfiguration;
                }
            }
        }

        if (remoteConfiguration == null) {
            fail("Remote Java application configuration was not created");
        }

        assertEquals(configuration.getName(), remoteConfiguration.getName());
        assertEquals(configuration.getHostName(), remoteConfiguration.HOST);
        assertEquals(configuration.getPort(), remoteConfiguration.PORT);
        assertFalse(remoteConfiguration.SERVER_MODE);
        assertEquals(TEST_CASE_MODULE_NAME, remoteConfiguration.getModules()[0].getName());
    }

    public void testWithUnknownModule() throws Exception {
        Map<String, String> connectMap = new HashMap<String, String>(2);
        connectMap.put(RemoteJavaApplicationConfiguration.HOST_NAME_KEY, "hostname");
        connectMap.put(RemoteJavaApplicationConfiguration.PORT_KEY, "9000");

        @SuppressWarnings("ConstantConditions")
        RemoteJavaApplicationConfiguration configuration = new RemoteJavaApplicationConfiguration("test-execute", connectMap, AddRemoteJavaApplicationCommand.SOCKET_ATTACH_CONNECTOR, "unknown");

        command = (AddRemoteJavaApplicationCommand) configuration.getCommand();

        Project project = getProject();

        try {
            command.execute(project);
            fail("The command should fail if the specified module does not exist");
        } catch (EclipserException ignored) {
        }
    }

    public void testWithDuplicateConfigurationName() throws Exception {
        Map<String, String> connectMap = new HashMap<String, String>(2);
        connectMap.put(RemoteJavaApplicationConfiguration.HOST_NAME_KEY, "hostname");
        connectMap.put(RemoteJavaApplicationConfiguration.PORT_KEY, "9000");

        @SuppressWarnings("ConstantConditions")
        RemoteJavaApplicationConfiguration configuration = new RemoteJavaApplicationConfiguration("test-execute", connectMap, AddRemoteJavaApplicationCommand.SOCKET_ATTACH_CONNECTOR, null);

        command = (AddRemoteJavaApplicationCommand) configuration.getCommand();

        Project project = getProject();

        command.execute(project);

        try {
            command.execute(project);
            fail("The command should fail when there's already a configuration with the same name");
        } catch (EclipserException ignored) {
        }
    }

    @Override
    protected void tearDown() throws Exception {
        RunManagerImpl manager = (RunManagerImpl) RunManager.getInstance(getProject());
        manager.clearAll();
        super.tearDown();
    }

}
