package com.kukido.eclipser.configuration;

import com.kukido.eclipser.command.AddApplicationConfigurationCommand;
import com.kukido.eclipser.command.Command;
import org.jetbrains.annotations.NotNull;

public class JavaConfiguration implements Configuration {

    public static final String MODULE_DIR = "$MODULE_DIR$";

    private String configurationName;
    private String mainClassName;
    private String moduleName;
    private String vmParameters;
    private String programParameters;
    @SuppressWarnings("FieldCanBeLocal")
	private String workingDirectory = MODULE_DIR;

    public JavaConfiguration(@NotNull String configurationName, @NotNull String mainClassName, @NotNull String moduleName, String vmParameters, String programParameters) {
        this.configurationName = configurationName;
        this.mainClassName = mainClassName;
        this.moduleName = moduleName;
        this.vmParameters = vmParameters;
        this.programParameters = programParameters;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public String getMainClassName() {
        return mainClassName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public String getVmParameters() {
        return vmParameters;
    }

    public String getProgramParameters() {
        return programParameters;
    }

    @Override
    public Command getCommand() {
        return new AddApplicationConfigurationCommand(this);
    }
}
