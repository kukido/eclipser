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
    public static final String MODULE_DIR_MACRO         = "$MODULE_DIR$";

    private String configurationName;

    // <stringAttribute key="M2_GOALS" value="clean install -DskipTests=true"/>
    private String commandLine;
    // <stringAttribute key="M2_PROFILES" value="localConfig,dependencies"/>
    private String[] profiles;
    // <booleanAttribute key="M2_WORKSPACE_RESOLUTION" value="false"/>
    private boolean resolveToWorkspace;
    private String workingDirectory;

    /*
    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <launchConfiguration type="org.eclipse.m2e.Maven2LaunchConfigurationType">
    <booleanAttribute key="M2_DEBUG_OUTPUT" value="false"/>
    <stringAttribute key="M2_GOALS" value="clean install -DskipTests=true"/>
    <booleanAttribute key="M2_NON_RECURSIVE" value="false"/>
    <booleanAttribute key="M2_OFFLINE" value="false"/>
    <stringAttribute key="M2_PROFILES" value="localConfig,dependencies"/>
    <listAttribute key="M2_PROPERTIES"/>
    <stringAttribute key="M2_RUNTIME" value="EMBEDDED"/>
    <booleanAttribute key="M2_SKIP_TESTS" value="false"/>
    <intAttribute key="M2_THREADS" value="1"/>
    <booleanAttribute key="M2_UPDATE_SNAPSHOTS" value="false"/>
    <booleanAttribute key="M2_WORKSPACE_RESOLUTION" value="false"/>
    <stringAttribute key="org.eclipse.debug.core.ATTR_REFRESH_SCOPE" value="${workspace}"/>
    <listAttribute key="org.eclipse.debug.ui.favoriteGroups">
    <listEntry value="org.eclipse.debug.ui.launchGroup.debug"/>
    <listEntry value="org.eclipse.debug.ui.launchGroup.run"/>
    </listAttribute>
    <booleanAttribute key="org.eclipse.jdt.launching.ATTR_USE_START_ON_FIRST_THREAD" value="true"/>
    <stringAttribute key="org.eclipse.jdt.launching.WORKING_DIRECTORY" value="${workspace_loc:/}"/>
    </launchConfiguration>
     */

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

