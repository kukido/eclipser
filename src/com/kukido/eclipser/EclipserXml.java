package com.kukido.eclipser;

import org.jetbrains.annotations.NonNls;

public interface EclipserXml {

    @NonNls String LAUNCH_EXT = "launch";
    @NonNls String KEY = "key";
    @NonNls String VALUE = "value";
    @NonNls String MAIN_TYPE_KEY = "org.eclipse.jdt.launching.MAIN_TYPE";
    @NonNls String PROJECT_ATTR_KEY = "org.eclipse.jdt.launching.PROJECT_ATTR";
    @NonNls String VM_ARGUMENTS_KEY = "org.eclipse.jdt.launching.VM_ARGUMENTS";
    @NonNls String ATTR_LOCATION_KEY = "org.eclipse.ui.externaltools.ATTR_LOCATION";
    @NonNls String ATTR_TOOL_ARGUMENTS_KEY = "org.eclipse.ui.externaltools.ATTR_TOOL_ARGUMENTS";

    // configuration type
    @NonNls String CONFIGURATION_TYPE_ANT                       = "org.eclipse.ant.AntLaunchConfigurationType";
    @NonNls String CONFIGURATION_TYPE_JUNIT                     = "org.eclipse.jdt.junit.launchconfig";
    @NonNls String CONFIGURATION_TYPE_LOCAL_JAVA_APPLICATION    = "org.eclipse.jdt.launching.localJavaApplication";
    @NonNls String CONFIGURATION_TYPE_PROGRAM_LAUNCH            = "org.eclipse.ui.externaltools.ProgramLaunchConfigurationType";

}
