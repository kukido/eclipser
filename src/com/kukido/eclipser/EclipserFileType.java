package com.kukido.eclipser;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;
import icons.EclipserIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class EclipserFileType implements FileType {
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
    public boolean isBinary() {
        return false;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Nullable
    @Override
    public String getCharset(@NotNull VirtualFile file, byte[] content) {
        return CharsetToolkit.UTF8;
    }
}
