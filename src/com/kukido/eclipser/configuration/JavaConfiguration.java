package com.kukido.eclipser.configuration;

import com.kukido.eclipser.command.AddApplicationConfigurationCommand;
import com.kukido.eclipser.command.Command;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class JavaConfiguration implements Configuration {

    public static final String MODULE_DIR_MACRO = "$MODULE_DIR$";
    private String configurationName;
    private String mainClassName;
    private String moduleName;
    private String vmParameters;
    private String programParameters;
    private Map<String, String> environmentVariables = new LinkedHashMap<String, String>();

    public JavaConfiguration(@NotNull String configurationName, @NotNull String mainClassName, @NotNull String moduleName, String vmParameters, String programParameters, Map<String, String> environmentVariables) {
        this.configurationName = configurationName;
        this.mainClassName = mainClassName;
        this.moduleName = moduleName;
        this.vmParameters = vmParameters;
        this.programParameters = programParameters;
        if (environmentVariables != null) {
            this.environmentVariables.putAll(environmentVariables);
        }
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
        return MODULE_DIR_MACRO;
    }

    public String getVmParameters() {
        return vmParameters;
    }

    public String getProgramParameters() {
        return programParameters;
    }

    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    @Override
    public Command getCommand() {
        return new AddApplicationConfigurationCommand(this);
    }
}
