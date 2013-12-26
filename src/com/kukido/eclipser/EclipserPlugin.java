package com.kukido.eclipser;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

public class EclipserPlugin implements ApplicationComponent {
    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "Eclipser.EclipserPlugin";
    }
}
