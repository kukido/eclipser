package com.kukido.eclipser.configuration;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.command.AddMaven2ConfigurationCommand;
import com.kukido.eclipser.command.Command;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Maven2Configuration implements Configuration {

    public static final String MAVEN_PLUGIN_IDENTIFIER  = "org.jetbrains.idea.maven";

    private String configurationName;

    private String commandLine;
    private String[] profiles;
    private boolean resolveToWorkspace;
    private String workingDirectory;

    public Maven2Configuration(@NotNull String configurationName, boolean resolveToWorkspace, String[] profiles, String commandLine, String workingDirectory) {
        this.configurationName = configurationName;
        this.resolveToWorkspace = resolveToWorkspace;
        this.profiles = profiles;
        this.commandLine = commandLine;
        this.workingDirectory = workingDirectory;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public boolean isResolveToWorkspace() {
        return resolveToWorkspace;
    }

    public String[] getProfiles() {
        return profiles;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public Map<String, Boolean> getProfilesMap() {
        Map<String, Boolean> profilesMap = new HashMap<String, Boolean>(profiles.length);
        for (String profile : profiles) {
            profilesMap.put(profile, Boolean.TRUE);
        }
        return profilesMap;
    }

    public List<String> getGoals() {
        return Arrays.asList(commandLine.split(" "));
    }

    @Override
    public Command getCommand() throws EclipserException {
        checkMavenPluginStatus();
        return new AddMaven2ConfigurationCommand(this);
    }

    private void checkMavenPluginStatus() throws EclipserException {
        PluginId pluginId = PluginId.getId(MAVEN_PLUGIN_IDENTIFIER);

        boolean installed = PluginManager.isPluginInstalled(pluginId);
        boolean enabled = false;

        if (installed) {
            IdeaPluginDescriptor descriptor = PluginManager.getPlugin(pluginId);
            assert descriptor != null;
            enabled = descriptor.isEnabled();
        }

        if (!installed) throw new EclipserException("Maven plugin is not installed. Please install Maven plugin to continue.");
        if (!enabled) throw new EclipserException("Maven plugin is installed, but not enabled. Please enable Maven plugin to continue.");
    }
}

