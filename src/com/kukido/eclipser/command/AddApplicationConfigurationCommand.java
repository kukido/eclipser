package com.kukido.eclipser.command;

import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.kukido.eclipser.EclipserConfigurationType;
import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.configuration.JavaConfiguration;
import org.apache.commons.lang.StringUtils;

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
            String message = "Could not find the module with name '" + moduleNameOfRunner + "'. You can either update Eclipse launch file with the correct name or create a new module." +
                    "\n\nHere is the list of modules that were found:\n   " + StringUtils.join(ModuleManager.getInstance(project).getModules(), "\n   ");
            throw new EclipserException(message);
        }

        Application application = ApplicationManager.getApplication();

        ApplicationConfiguration applicationConfiguration;

        RunManagerImpl runManager = (RunManagerImpl) RunManager.getInstance(project);

        RunnerAndConfigurationSettingsImpl runnerAndConfigurationSettings = findConfigurationByName(javaConfiguration.getConfigurationName(), runManager);

        if (runnerAndConfigurationSettings != null) {
            applicationConfiguration = (ApplicationConfiguration) runnerAndConfigurationSettings.getConfiguration();
        } else {
            EclipserConfigurationType type = application.getComponent(EclipserConfigurationType.class);
            runnerAndConfigurationSettings = (RunnerAndConfigurationSettingsImpl) runManager.createRunConfiguration(javaConfiguration.getConfigurationName(), type.getConfigurationFactories()[0]);
            applicationConfiguration = (ApplicationConfiguration) runnerAndConfigurationSettings.getConfiguration();
            runManager.addConfiguration(runnerAndConfigurationSettings, true);
        }

        applicationConfiguration.setModule(module);
        applicationConfiguration.setMainClassName(javaConfiguration.getMainClassName());
        applicationConfiguration.setWorkingDirectory(javaConfiguration.getWorkingDirectory());
        applicationConfiguration.setVMParameters(javaConfiguration.getVmParameters());
        applicationConfiguration.setProgramParameters(javaConfiguration.getProgramParameters());

        runManager.setSelectedConfiguration(runnerAndConfigurationSettings);
    }

    private RunnerAndConfigurationSettingsImpl findConfigurationByName(String name, RunManagerImpl runManager){
        for (RunnerAndConfigurationSettings settings : runManager.getSortedConfigurations()){
            if (settings.getName().equals(name))
                return (RunnerAndConfigurationSettingsImpl) settings;
        }
        return null;
    }
}
