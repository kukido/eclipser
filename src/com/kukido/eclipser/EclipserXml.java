package com.kukido.eclipser;

import org.jetbrains.annotations.NonNls;

public interface EclipserXml {

    @NonNls String LAUNCH_EXT = "launch";
    @NonNls String KEY = "key";
    @NonNls String VALUE = "value";
    @NonNls String MAIN_TYPE_KEY = "org.eclipse.jdt.launching.MAIN_TYPE";
    @NonNls String PROJECT_ATTR_KEY = "org.eclipse.jdt.launching.PROJECT_ATTR";
    @NonNls String VM_ARGUMENTS_KEY = "org.eclipse.jdt.launching.VM_ARGUMENTS";
}
