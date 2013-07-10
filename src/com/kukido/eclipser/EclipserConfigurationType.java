package com.kukido.eclipser;

import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.openapi.components.ApplicationComponent;
import icons.EclipserIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class EclipserConfigurationType extends ApplicationConfigurationType implements ApplicationComponent {
    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    public String getDisplayName() {
        return "My eclipser";
    }

    public String getConfigurationTypeDescription() {
        return "Runs a launch file";
    }

    public Icon getIcon() {
        return EclipserIcons.Launch;
    }

    @NotNull
    public String getId() {
        return "#eclipser";
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "eclipser component";
    }
}
