package com.kukido.eclipser.configuration;

import com.kukido.eclipser.command.AddExternalToolCommand;
import com.kukido.eclipser.command.Command;

public class ExternalToolConfiguration implements  Configuration {

    public static final String PROJECT_FILE_DIR = "$ProjectFileDir$";

    private String name;
    private String program;
    private String parameters;
    private String workingDirectory;

    public ExternalToolConfiguration(String name, String program, String parameters, String workingDirectory) {
        this.name = name;
        this.program = program;
        this.parameters = parameters;
        this.workingDirectory = workingDirectory;
    }

    public String getProgram() {
        return program;
    }

    public String getParameters() {
        return parameters;
    }

    public String getName() {
        return name;
    }

    public String getWorkingDirectory() {
        if (workingDirectory == null) return PROJECT_FILE_DIR;
        else return workingDirectory;
    }

    @Override
    public Command getCommand() {
        return new AddExternalToolCommand(this);
    }
}
