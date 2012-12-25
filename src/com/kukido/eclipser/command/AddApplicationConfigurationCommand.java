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
import com.intellij.openapi.ui.Messages;
import com.kukido.eclipser.EclipserConfigurationType;
import com.kukido.eclipser.configuration.JavaConfiguration;
import org.apache.commons.lang.StringUtils;

public class AddApplicationConfigurationCommand implements Command {

    private JavaConfiguration javaConfiguration;

    public AddApplicationConfigurationCommand(JavaConfiguration conf) {
        this.javaConfiguration = conf;
    }

    @Override
    public void execute(Project project) {
        createRunConfiguration(project);
    }

    private void createRunConfiguration(Project project) {

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

        String moduleNameOfRunner = javaConfiguration.getModuleName();

        Module module = ModuleManager.getInstance(project).findModuleByName(moduleNameOfRunner);

        if (module == null) {
            say("Could not find the module of the runner with name '" + moduleNameOfRunner + "'. Check settings." +
                    "\n\nHere is the list of modules that were found:\n   " + StringUtils.join(ModuleManager.getInstance(project).getModules(), "\n   "));
            return;
        }

        applicationConfiguration.setModule(module);
        applicationConfiguration.setMainClassName(javaConfiguration.getMainClassName());
        applicationConfiguration.setWorkingDirectory(javaConfiguration.getWorkingDirectory());
        applicationConfiguration.setVMParameters(javaConfiguration.getVmParameters());

        runManager.setSelectedConfiguration(runnerAndConfigurationSettings);
    }

    private RunnerAndConfigurationSettingsImpl findConfigurationByName(String name, RunManagerImpl runManager){
        for (RunnerAndConfigurationSettings settings : runManager.getSortedConfigurations()){
            if (settings.getName().equals(name))
                return (RunnerAndConfigurationSettingsImpl) settings;
        }
        return null;
    }

    public void say(String message) {
        Messages.showMessageDialog(message, "Info", Messages.getInformationIcon());
    }
}
