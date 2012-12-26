package com.kukido.eclipser.command;

import com.intellij.openapi.project.Project;
import com.intellij.tools.Tool;
import com.intellij.tools.ToolManager;
import com.intellij.tools.ToolsGroup;
import com.kukido.eclipser.configuration.ExternalToolConfiguration;

import java.util.ArrayList;
import java.util.Collection;

public class AddExternalToolCommand implements Command {

    public static final String DEFAULT_GROUP_NAME = "Converted";

    private ExternalToolConfiguration configuration;

    public AddExternalToolCommand(ExternalToolConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void execute(Project project) {
        ToolManager manager = ToolManager.getInstance();

        Collection<ToolsGroup> groups = new ArrayList<ToolsGroup>();

        EclipserTool tool = new EclipserTool();

        tool.setName(configuration.getName());
        tool.setShownInMainMenu(true);
        tool.setEnabled(true);
        tool.setProgram(configuration.getProgram());
        tool.setParameters(configuration.getParameters());
        tool.setWorkingDirectory(configuration.getWorkingDirectory());
        tool.setGroupName(DEFAULT_GROUP_NAME);

        ToolsGroup group = new ToolsGroup(DEFAULT_GROUP_NAME);
        group.addElement(tool);

        groups.add(group);

        manager.setTools(groups.toArray(new ToolsGroup[groups.size()]));
    }

    private class EclipserTool extends Tool {

        private String name;
        private boolean shownInMainMenu;
        private boolean enabled;

        @Override
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setShownInMainMenu(boolean shownInMainMenu) {
            this.shownInMainMenu = shownInMainMenu;
        }

        @Override
        public boolean isShownInMainMenu() {
            return shownInMainMenu;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

}
