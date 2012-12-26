package com.kukido.eclipser.configuration;

import com.kukido.eclipser.command.AddExternalToolCommand;
import com.kukido.eclipser.command.Command;

public class ExternalToolConfiguration implements  Configuration {

    public static final String PROJECT_FILE_DIR = "$ProjectFileDir$";

    private String name;
    private String program;
    private String parameters;

    public ExternalToolConfiguration(String name, String program, String parameters) {
        this.name = name;
        this.program = program;
        this.parameters = parameters;
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
        return PROJECT_FILE_DIR;
    }

    @Override
    public Command getCommand() {
        return new AddExternalToolCommand(this);
    }
}
