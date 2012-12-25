package com.kukido.eclipser.configuration;

import com.kukido.eclipser.command.AddApplicationConfigurationCommand;
import com.kukido.eclipser.command.Command;

public class JavaConfiguration implements Configuration {

    public static final String MODULE_DIR = "$MODULE_DIR$";

    private String configurationName;
    private String mainType;
    private String moduleName;
    private String vmParameters;
    private String workingDirectory = MODULE_DIR;

    public JavaConfiguration(String configurationName, String mainType, String moduleName, String vmParameters) {
        this.configurationName = configurationName;
        this.mainType = mainType;
        this.moduleName = moduleName;
        this.vmParameters = vmParameters;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public String getMainType() {
        return mainType;
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

    @Override
    public Command getCommand() {
        return new AddApplicationConfigurationCommand(this);
    }
}
