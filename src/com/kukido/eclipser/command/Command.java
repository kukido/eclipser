package com.kukido.eclipser.command;

import com.intellij.openapi.project.Project;

public interface Command {
    public void execute(Project context);
}
