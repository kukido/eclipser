package com.kukido.eclipser.command;

import com.intellij.testFramework.LightIdeaTestCase;
import com.intellij.tools.Tool;
import com.intellij.tools.ToolManager;
import com.intellij.tools.ToolsGroup;
import com.kukido.eclipser.configuration.ExternalToolConfiguration;

import java.util.List;

public class AddExternalToolCommandTest extends LightIdeaTestCase {

    private AddExternalToolCommand command;

    public void testExecuteWithFullConfiguration() {

        ExternalToolConfiguration configuration = new ExternalToolConfiguration(
            "full-configuration",
            "foo.sh",
            "--port 11111",
            "/user/bar/foo"
        );

        command = new AddExternalToolCommand(configuration);
        command.execute(null);

        ToolManager toolManager = ToolManager.getInstance();

        List<ToolsGroup<Tool>> toolsGroups = toolManager.getGroups();

        ToolsGroup toolsGroup = null;

        for (ToolsGroup tg : toolsGroups) {
            if (tg.getName().equals(AddExternalToolCommand.DEFAULT_GROUP_NAME)) {
                toolsGroup = tg;
                break;
            }
        }

        if (toolsGroup == null) fail("Eclipser group was not created");

        List<Tool> tools = toolManager.getTools(AddExternalToolCommand.DEFAULT_GROUP_NAME);
        assertEquals(1, tools.size());

        Tool tool = tools.get(0);

        assertEquals(configuration.getName(), tool.getName());
        assertEquals(configuration.getProgram(), tool.getProgram());
        assertEquals(configuration.getParameters(), tool.getParameters());
        assertEquals(configuration.getWorkingDirectory(), tool.getWorkingDirectory());
    }

    public void testExecuteWithMinimalConfiguration() {

        ExternalToolConfiguration configuration = new ExternalToolConfiguration(
                "minimal-configuration",
                "foo.sh",
                null,
                null
        );

        command = new AddExternalToolCommand(configuration);
        command.execute(null);

        ToolManager toolManager = ToolManager.getInstance();

        List<ToolsGroup<Tool>> toolsGroups = toolManager.getGroups();

        ToolsGroup toolsGroup = null;

        for (ToolsGroup tg : toolsGroups) {
            if (tg.getName().equals(AddExternalToolCommand.DEFAULT_GROUP_NAME)) {
                toolsGroup = tg;
                break;
            }
        }

        if (toolsGroup == null) fail("Eclipser group was not created");

        List<Tool> tools = toolManager.getTools(AddExternalToolCommand.DEFAULT_GROUP_NAME);
        assertEquals(1, tools.size());

        Tool tool = tools.get(0);

        assertEquals(configuration.getName(), tool.getName());
        assertEquals(configuration.getProgram(), tool.getProgram());
        assertEquals(ExternalToolConfiguration.PROJECT_FILE_DIR, tool.getWorkingDirectory());
    }

    @Override
    protected void tearDown() throws Exception {
        ToolManager.getInstance().setTools(new ToolsGroup[]{});
        super.tearDown();
    }
}
