package com.kukido.eclipser.command;

import com.intellij.execution.RunManager;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.lang.ant.config.AntBuildFile;
import com.intellij.lang.ant.config.AntBuildListener;
import com.intellij.lang.ant.config.AntBuildModel;
import com.intellij.lang.ant.config.AntBuildTarget;
import com.intellij.lang.ant.config.AntConfiguration;
import com.intellij.lang.ant.config.AntNoFileException;
import com.intellij.lang.ant.config.execution.AntRunConfiguration;
import com.intellij.lang.ant.config.execution.AntRunConfigurationType;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.configuration.AntTargetConfiguration;
import com.kukido.eclipser.configuration.Configuration;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AddAntTargetCommand implements Command {

    private final AntTargetConfiguration configuration;

    public AddAntTargetCommand(AntTargetConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void execute(Project project) throws EclipserException {
        AntConfiguration c = AntConfiguration.getInstance(project);
        String url = String.format("file://%s", configuration.getLocation());
        VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl(url);

        if (virtualFile == null) throw new EclipserException("Unable to locate Ant build file: " + url);

        AntBuildFile antBuildFile;
        try {
            antBuildFile = c.addBuildFile(virtualFile);
        } catch (AntNoFileException antNoFileException) {
            throw new EclipserException(antNoFileException.getMessage());
        }

        String target = antBuildFile.getModel().getDefaultTargetName();
        if (target == null) throw new EclipserException("Unable to add Ant Target configuration. Default target is not defined in Ant build file.");

        AntRunConfiguration antRunConfiguration;

        RunManagerImpl runManager = (RunManagerImpl) RunManager.getInstance(project);

        AntRunConfigurationType type = AntRunConfigurationType.getInstance();
        RunnerAndConfigurationSettingsImpl runnerAndConfigurationSettings = (RunnerAndConfigurationSettingsImpl) runManager.createConfiguration(configuration.getName(), type.getConfigurationFactories()[0]);
        antRunConfiguration = (AntRunConfiguration) runnerAndConfigurationSettings.getConfiguration();
        runManager.addConfiguration(runnerAndConfigurationSettings, Configuration.SHARE_RUN_CONFIGURATION_DEFAULT_SETTING);
        AntBuildTarget antBuildTarget = new BuildTarget(antBuildFile, target);
        antRunConfiguration.acceptSettings(antBuildTarget);
        runManager.setSelectedConfiguration(runnerAndConfigurationSettings);
    }

    private class BuildTarget implements AntBuildTarget {

        private final AntBuildFile file;
        private final String name;

        public BuildTarget(AntBuildFile file, String name) {
            this.file = file;
            this.name = name;
        }

        @Nullable
        @Override
        public String getName() {
            return name;
        }

        @Nullable
        @Override
        public String getDisplayName() {
            return null;
        }

        @Nullable
        @Override
        public String getNotEmptyDescription() {
            return null;
        }

        @Override
        public boolean isDefault() {
            return false;
        }

        @Override
        public void run(DataContext dataContext, List list, AntBuildListener antBuildListener) {
        }

        @Override
        public AntBuildModel getModel() {
            return file.getModel();
        }
    }
}
