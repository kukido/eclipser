package com.kukido.eclipser;

import org.jetbrains.annotations.NonNls;

public interface EclipserXml {

    @NonNls String LAUNCH_EXT = "launch";
    @NonNls String TYPE = "type";
    @NonNls String KEY = "key";
    @NonNls String VALUE = "value";
    @NonNls String MAIN_TYPE_KEY = "org.eclipse.jdt.launching.MAIN_TYPE";
    @NonNls String PROJECT_ATTR_KEY = "org.eclipse.jdt.launching.PROJECT_ATTR";
    @NonNls String VM_ARGUMENTS_KEY = "org.eclipse.jdt.launching.VM_ARGUMENTS";
    @NonNls String PROGRAM_ARGUMENTS_KEY = "org.eclipse.jdt.launching.PROGRAM_ARGUMENTS";
    @NonNls String ATTR_LOCATION_KEY = "org.eclipse.ui.externaltools.ATTR_LOCATION";
    @NonNls String ATTR_TOOL_ARGUMENTS_KEY = "org.eclipse.ui.externaltools.ATTR_TOOL_ARGUMENTS";
    @NonNls String ATTR_WORKING_DIRECTORY_KEY = "org.eclipse.ui.externaltools.ATTR_WORKING_DIRECTORY";
    @NonNls String M2_GOALS_KEY = "M2_GOALS";
    @NonNls String M2_PROFILES_KEY = "M2_PROFILES";
    @NonNls String M2_WORKSPACE_RESOLUTION = "M2_WORKSPACE_RESOLUTION";
    @NonNls String WORKING_DIRECTORY_KEY = "org.eclipse.jdt.launching.WORKING_DIRECTORY";
    @NonNls String ENVIRONMENT_VARIABLES_KEY = "org.eclipse.debug.core.environmentVariables";
    @NonNls String CONNECT_MAP_KEY = "org.eclipse.jdt.launching.CONNECT_MAP";
    @NonNls String VM_CONNECTOR_ID_KEY = "org.eclipse.jdt.launching.VM_CONNECTOR_ID";

    // attribute type
    @NonNls String BOOLEAN_ATTRIBUTE = "booleanAttribute";
    @SuppressWarnings("UnusedDeclaration")
    @NonNls String LIST_ATTRIBUTE = "listAttribute";
    @NonNls String MAP_ATTRIBUTE = "mapAttribute";
    @NonNls String STRING_ATTRIBUTE = "stringAttribute";
    @NonNls String MAP_ENTRY_ATTRIBUTE = "mapEntry";

    // configuration type
    @NonNls String CONFIGURATION_TYPE_ANT_LAUNCH                = "org.eclipse.ant.AntLaunchConfigurationType";
    @NonNls String CONFIGURATION_TYPE_LOCAL_JAVA_APPLICATION    = "org.eclipse.jdt.launching.localJavaApplication";
    @NonNls String CONFIGURATION_TYPE_MAVEN2_LAUNCH             = "org.eclipse.m2e.Maven2LaunchConfigurationType";
    @NonNls String CONFIGURATION_TYPE_PROGRAM_LAUNCH            = "org.eclipse.ui.externaltools.ProgramLaunchConfigurationType";
    @NonNls String CONFIGURATION_TYPE_REMOTE_JAVA_APPLICATION   = "org.eclipse.jdt.launching.remoteJavaApplication";
    // unsupported
    @NonNls String CONFIGURATION_TYPE_GWT_WEB_APPLICATION       = "com.google.gdt.eclipse.suite.webapp";
    @NonNls String CONFIGURATION_TYPE_JUNIT_LAUNCH              = "org.eclipse.jdt.junit.launchconfig";
    @NonNls String CONFIGURATION_TYPE_RUNTIME_WORKBENCH         = "org.eclipse.pde.ui.RuntimeWorkbench";

    // eclipse macros
    @NonNls String WORKSPACE_LOC    = "workspace_loc";
    @NonNls String PROJECT_LOC      = "project_loc";
    @NonNls String ENV_VAR          = "env_var";

}
