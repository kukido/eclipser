package com.kukido.eclipser;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class EclipserFileTypeFactory extends FileTypeFactory {

    private final static FileType fileType = new EclipserFileType();

    @Override
    public void createFileTypes(@NotNull FileTypeConsumer consumer) {
        consumer.consume(fileType, EclipserXml.LAUNCH_EXT);
    }
}
