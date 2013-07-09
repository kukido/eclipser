package com.kukido.eclipser.command;

import com.intellij.openapi.project.Project;
import com.intellij.tools.Tool;
import com.intellij.tools.ToolManager;
import com.intellij.tools.ToolsGroup;
import com.kukido.eclipser.configuration.ExternalToolConfiguration;

import java.util.ArrayList;
import java.util.List;

public class AddExternalToolCommand implements Command {

    public static final String DEFAULT_GROUP_NAME = "Eclipser";

    private ExternalToolConfiguration configuration;

    public AddExternalToolCommand(ExternalToolConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void execute(Project project) {
        ToolManager manager = ToolManager.getInstance();

        ToolsGroup<Tool> target = null;

        List<ToolsGroup<Tool>> groups = new ArrayList<ToolsGroup<Tool>>(manager.getGroups());

        for (ToolsGroup<Tool> group : groups) {
            if (group.getName().equalsIgnoreCase(DEFAULT_GROUP_NAME)) {
                target = group;
            }
        }

        if (target == null) {
            target = new ToolsGroup<Tool>(DEFAULT_GROUP_NAME);
            groups.add(target);
        }

        EclipserTool tool = new EclipserTool();

        tool.setName(configuration.getName());
        tool.setShownInMainMenu(true);
        tool.setEnabled(true);
        tool.setUseConsole(true);
        tool.setProgram(configuration.getProgram());
        tool.setParameters(configuration.getParameters());
        tool.setWorkingDirectory(configuration.getWorkingDirectory());
        tool.setGroupName(DEFAULT_GROUP_NAME);

        target.addElement(tool);

        manager.setTools(groups.toArray(new ToolsGroup[groups.size()]));
    }

    static class EclipserTool extends Tool {

        private String name;
        private boolean shownInMainMenu;
        private boolean enabled;
        private boolean useConsole;

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

        public void setUseConsole(boolean useConsole) {
            this.useConsole = useConsole;
        }

        @Override
        public boolean isUseConsole() {
            return useConsole;
        }

        @Override
        public String getActionId() {

            StringBuilder id = new StringBuilder(ACTION_ID_PREFIX);

            String group = getGroup();

            if (group != null) {
                id.append(group);
                id.append('_');
            }
            if (name != null) {
                id.append(name);
            }
            return id.toString();
        }

		@SuppressWarnings("RedundantIfStatement")
		@Override
		public boolean equals(Object o)
		{
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			if (!super.equals(o)) return false;

			EclipserTool that = (EclipserTool) o;

			if (enabled != that.enabled) return false;
			if (shownInMainMenu != that.shownInMainMenu) return false;
			if (useConsole != that.useConsole) return false;
			if (name != null ? !name.equals(that.name) : that.name != null) return false;

			return true;
		}

		@Override
		public int hashCode()
		{
			int result = name != null ? name.hashCode() : 0;
			result = 31 * result + (shownInMainMenu ? 1 : 0);
			result = 31 * result + (enabled ? 1 : 0);
			result = 31 * result + (useConsole ? 1 : 0);
			return result;
		}
	}
}