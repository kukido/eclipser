package com.kukido.eclipser.command;

import com.intellij.tools.Enabler;
import com.intellij.tools.Tool;
import com.intellij.tools.ToolManager;
import com.intellij.tools.ToolsGroup;

import java.util.ArrayList;
import java.util.Collection;

public class AddExternalToolCommand implements Command {

    @Override
    public void execute() {
        String name = "";
        String program = "";
        String arguments = "";

        ToolManager manager = ToolManager.getInstance();

        Collection<ToolsGroup> groups = new ArrayList<ToolsGroup>();

        Enabler enabler = new Enabler("Eclipser", true, true);

        Tool tool = enabler.create();

        tool.setProgram("/kafka/kafka/bin/zookeeper-server-start.sh");
        tool.setParameters("/kafka/kafka/config/zookeeper.properties");
        tool.setGroupName("Converted");

        ToolsGroup group = new ToolsGroup("Converted");
        group.addElement(tool);

        groups.add(group);

        manager.setTools(groups.toArray(new ToolsGroup[groups.size()]));
    }
}
