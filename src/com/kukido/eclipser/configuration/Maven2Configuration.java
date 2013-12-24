package com.kukido.eclipser.configuration;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.command.AddMaven2ConfigurationCommand;
import com.kukido.eclipser.command.Command;

public class Maven2Configuration implements Configuration, PreFlight {

    public static final String MAVEN_PLUGIN_IDENTIFIER = "org.jetbrains.idea.maven";

    @Override
    public Command getCommand() {
        return new AddMaven2ConfigurationCommand();
    }

    @Override
    public void check() throws EclipserException {
        // check for Maven plugin installed
        // if plugin is not installed or not enabled, throw an exception

        PluginId pluginId = PluginId.getId(MAVEN_PLUGIN_IDENTIFIER);

        boolean mavenPluginInstalled = PluginManager.isPluginInstalled(pluginId);
        boolean mavenPluginEnabled = false;

        if (mavenPluginInstalled) {
            IdeaPluginDescriptor descriptor = PluginManager.getPlugin(pluginId);
            mavenPluginEnabled = descriptor.isEnabled();
        }

        String status = mavenPluginInstalled ? "installed" : "not installed";
        System.out.println("Maven plugin status: " + status + ", enabled: " + mavenPluginEnabled);

        // todo: refactor
        if (!mavenPluginInstalled) throw new EclipserException("Maven plugin is not installed.");
        if (!mavenPluginEnabled) throw new EclipserException("Maven plugin is installed, but not enabled.");
    }
}

