package com.kukido.eclipser.command;

import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.openapi.project.Project;
import com.kukido.eclipser.EclipserException;
import org.jetbrains.idea.maven.execution.MavenRunConfigurationType;
import org.jetbrains.idea.maven.execution.MavenRunnerParameters;

public class AddMaven2ConfigurationCommand implements Command {

    @Override
    public void execute(Project project) throws EclipserException {
        MavenRunnerParameters parameters = new MavenRunnerParameters();

        /*
        MavenRunConfiguration runConfiguration = (MavenRunConfiguration)settings.getConfiguration();
        runConfiguration.setRunnerParameters(params);
        runConfiguration.setGeneralSettings(generalSettings);
        runConfiguration.setRunnerSettings(runnerSettings);
        */

        RunnerAndConfigurationSettings settings = MavenRunConfigurationType.createRunnerAndConfigurationSettings(null, null, parameters, project);

        RunManagerImpl runManager = (RunManagerImpl) RunManager.getInstance(project);
        runManager.addConfiguration(settings, false);
    }
}
