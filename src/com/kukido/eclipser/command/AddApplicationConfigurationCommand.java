package com.kukido.eclipser.command;

import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.configuration.JavaConfiguration;

public class AddApplicationConfigurationCommand implements Command {

    private JavaConfiguration javaConfiguration;

    public AddApplicationConfigurationCommand(JavaConfiguration conf) {
        this.javaConfiguration = conf;
    }

    @Override
    public void execute(Project project) throws EclipserException {
        createRunConfiguration(project);
    }

    private void createRunConfiguration(Project project) throws EclipserException {

        String moduleNameOfRunner = javaConfiguration.getModuleName();

        Module module = ModuleManager.getInstance(project).findModuleByName(moduleNameOfRunner);

        if (module == null) {
            String message = "Could not find the module with name '" + moduleNameOfRunner + "'. You can either update Eclipse launch file with the correct name or create a new module.";
            throw new EclipserException(message);
        }

        ApplicationConfiguration applicationConfiguration;

        RunManagerImpl runManager = (RunManagerImpl) RunManager.getInstance(project);

        RunnerAndConfigurationSettingsImpl runnerAndConfigurationSettings = findConfigurationByName(javaConfiguration.getConfigurationName(), runManager);

        if (runnerAndConfigurationSettings != null) {
            String message = "Runtime configuration with name '" + javaConfiguration.getConfigurationName() + "' already exists. You can either rename it or delete to be replaced.";
            throw new EclipserException(message);
        } else {
            ApplicationConfigurationType type = ApplicationConfigurationType.getInstance();
            assert type != null;
            runnerAndConfigurationSettings = (RunnerAndConfigurationSettingsImpl) runManager.createRunConfiguration(javaConfiguration.getConfigurationName(), type.getConfigurationFactories()[0]);
            applicationConfiguration = (ApplicationConfiguration) runnerAndConfigurationSettings.getConfiguration();
            runManager.addConfiguration(runnerAndConfigurationSettings, true);
        }

        applicationConfiguration.setModule(module);
        applicationConfiguration.setMainClassName(javaConfiguration.getMainClassName());
        applicationConfiguration.setWorkingDirectory(javaConfiguration.getWorkingDirectory());
        applicationConfiguration.setVMParameters(javaConfiguration.getVmParameters());
        applicationConfiguration.setProgramParameters(javaConfiguration.getProgramParameters());
        applicationConfiguration.setEnvs(javaConfiguration.getEnvironmentVariables());

        runManager.setSelectedConfiguration(runnerAndConfigurationSettings);
    }

    private RunnerAndConfigurationSettingsImpl findConfigurationByName(String name, RunManagerImpl runManager) {
        for (RunnerAndConfigurationSettings settings : runManager.getSortedConfigurations()) {
            if (settings.getName().equals(name))
                return (RunnerAndConfigurationSettingsImpl) settings;
        }
        return null;
    }
}
