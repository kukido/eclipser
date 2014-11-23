package com.kukido.eclipser.command;

import com.intellij.openapi.project.Project;
import com.kukido.eclipser.EclipserException;

public interface Command {
    public void execute(Project project) throws EclipserException;
}
