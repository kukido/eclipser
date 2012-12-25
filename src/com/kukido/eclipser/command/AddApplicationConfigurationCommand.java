package com.kukido.eclipser.command;

import com.kukido.eclipser.configuration.JavaConfiguration;

public class AddApplicationConfigurationCommand implements Command {

    private JavaConfiguration conf;

    public AddApplicationConfigurationCommand(JavaConfiguration conf) {
        this.conf = conf;
    }

    @Override
    public void execute() {
        // create run configuration here




    }
}
