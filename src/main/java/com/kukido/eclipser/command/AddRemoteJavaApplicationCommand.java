package com.kukido.eclipser.command;

import com.intellij.execution.RunManager;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.execution.remote.RemoteConfiguration;
import com.intellij.execution.remote.RemoteConfigurationType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.configuration.RemoteJavaApplicationConfiguration;

public class AddRemoteJavaApplicationCommand implements Command {

    public static final String SOCKET_ATTACH_CONNECTOR = "org.eclipse.jdt.launching.socketAttachConnector";
    private static final String SOCKET_LISTEN_CONNECTOR = "org.eclipse.jdt.launching.socketListenConnector";

    private final RemoteJavaApplicationConfiguration configuration;

    public AddRemoteJavaApplicationCommand(RemoteJavaApplicationConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void execute(Project project) throws EclipserException {
        RemoteConfiguration remoteConfiguration;

        Module module = null;
        if (!isNullOrEmpty(configuration.getModuleName())) {
            module = ModuleManager.getInstance(project).findModuleByName(configuration.getModuleName());
            if (module == null) {
                String message = "Could not find the module with name '" + configuration.getModuleName() + "'. You can either update Eclipse launch file with the correct name or create a new module.";
                throw new EclipserException(message);
            }
        }

        RunManagerImpl runManager = (RunManagerImpl) RunManager.getInstance(project);

        RunnerAndConfigurationSettingsImpl runnerAndConfigurationSettings = (RunnerAndConfigurationSettingsImpl) runManager.findConfigurationByName(configuration.getName());

        if (runnerAndConfigurationSettings != null) {
            String message = "Runtime configuration with name '" + configuration.getName() + "' already exists. You can either rename it or delete to be replaced.";
            throw new EclipserException(message);
        } else {
            RemoteConfigurationType remoteConfigurationType = RemoteConfigurationType.getInstance();
            runnerAndConfigurationSettings = (RunnerAndConfigurationSettingsImpl) runManager.createRunConfiguration(configuration.getName(), remoteConfigurationType.getConfigurationFactories()[0]);
            remoteConfiguration = (RemoteConfiguration) runnerAndConfigurationSettings.getConfiguration();
            runManager.addConfiguration(runnerAndConfigurationSettings);
        }

        remoteConfiguration.setModule(module);
        remoteConfiguration.HOST = configuration.getHostName();
        remoteConfiguration.PORT = configuration.getPort();
        remoteConfiguration.SERVER_MODE = configuration.getVmConnectorId().equalsIgnoreCase(SOCKET_LISTEN_CONNECTOR);

        runManager.setSelectedConfiguration(runnerAndConfigurationSettings);
    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
