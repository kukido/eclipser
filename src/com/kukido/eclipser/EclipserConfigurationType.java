package com.kukido.eclipser;

import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.util.IconLoader;
import icons.EclipserIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: andrey
 * Date: 12/18/12                                                                   `
 * Time: 11:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class EclipserConfigurationType extends ApplicationConfigurationType implements ApplicationComponent {
    @Override
    public void initComponent() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void disposeComponent() {
        //To change body of implemented methods use File | Settings | File Templates.
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
        return "eclipser component";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
