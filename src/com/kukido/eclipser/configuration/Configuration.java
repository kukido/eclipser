package com.kukido.eclipser.configuration;

import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.command.Command;

public interface Configuration {
    public static final boolean SHARE_RUN_CONFIGURATION_DEFAULT_SETTING = false;
    public Command getCommand() throws EclipserException;
}