package com.kukido.eclipser.configuration;

import com.kukido.eclipser.command.AddExternalToolCommand;
import com.kukido.eclipser.command.Command;
import org.jetbrains.annotations.NotNull;

public class ExternalToolConfiguration implements Configuration {

    public static final String PROJECT_FILE_DIR = "$ProjectFileDir$";

    private final String name;
    private final String program;
    private final String parameters;
    private final String workingDirectory;

    public ExternalToolConfiguration(@NotNull String name, @NotNull String program, String parameters, String workingDirectory) {
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
