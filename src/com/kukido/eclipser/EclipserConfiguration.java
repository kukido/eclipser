package com.kukido.eclipser;

public class EclipserConfiguration {

    private String configurationName;
    private String mainClassName;
    private String workingDirectory;
    private String vmParameters;
    private String moduleName;

    public String getConfigurationName() {
        return configurationName;
    }

    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    public String getMainClassName() {
        return mainClassName;
    }

    public void setMainClassName(String mainClassName) {
        this.mainClassName = mainClassName;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public String getVmParameters() {
        return vmParameters;
    }

    public void setVmParameters(String vmParameters) {
        this.vmParameters = vmParameters;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
