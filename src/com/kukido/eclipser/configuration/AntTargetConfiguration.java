package com.kukido.eclipser.configuration;

import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.command.AddAntTargetCommand;
import com.kukido.eclipser.command.Command;
import org.jetbrains.annotations.NotNull;

public class AntTargetConfiguration implements Configuration {

    private final String name;
    private final String location;

    public AntTargetConfiguration(@NotNull String name, @NotNull String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public Command getCommand() throws EclipserException {
        return new AddAntTargetCommand(this);
    }
}
