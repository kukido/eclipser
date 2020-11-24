package com.kukido.eclipser.configuration;

import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.command.Command;

public interface Configuration {
    public Command getCommand() throws EclipserException;
}