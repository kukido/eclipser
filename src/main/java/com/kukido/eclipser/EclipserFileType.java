package com.kukido.eclipser;

import com.intellij.ide.highlighter.XmlLikeFileType;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;
import com.kukido.eclipser.icons.EclipserIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

class EclipserFileType extends XmlLikeFileType {

    public EclipserFileType() {
        super(XMLLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Eclipser";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Eclipse launch file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return EclipserXml.LAUNCH_EXT;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return EclipserIcons.Launch;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Nullable
    @Override
    public String getCharset(@NotNull VirtualFile file, @NotNull byte[] content) {
        return CharsetToolkit.UTF8;
    }
}
