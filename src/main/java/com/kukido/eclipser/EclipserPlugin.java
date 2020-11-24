package com.kukido.eclipser;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

class EclipserPlugin implements ApplicationComponent {
    @NotNull
    @Override
    public String getComponentName() {
        return "Eclipser.EclipserPlugin";
    }
}
