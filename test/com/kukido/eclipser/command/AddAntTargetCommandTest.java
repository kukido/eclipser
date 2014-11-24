package com.kukido.eclipser.command;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.lang.ant.config.AntBuildFile;
import com.intellij.lang.ant.config.AntConfiguration;
import com.intellij.lang.ant.config.execution.AntRunConfiguration;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.LightIdeaTestCase;
import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.configuration.AntTargetConfiguration;

import java.net.URL;
import java.util.List;

public class AddAntTargetCommandTest extends LightIdeaTestCase {

    private AddAntTargetCommand command;

    public void testExecute() throws Exception {

        URL resource = this.getClass().getClassLoader().getResource("resources/xml/build.xml");

        @SuppressWarnings("ConstantConditions")
        AntTargetConfiguration configuration = new AntTargetConfiguration("test-execute", resource.getPath());

        command = (AddAntTargetCommand) configuration.getCommand();

        Project project = getProject();

        command.execute(project);

        AntConfiguration antConfiguration = AntConfiguration.getInstance(project);
        AntBuildFile[] antBuildFiles = antConfiguration.getBuildFiles();
        assertEquals(1, antBuildFiles.length);

        AntRunConfiguration antRunConfiguration = getAntRunConfiguration(configuration.getName());
        assertNotNull(antRunConfiguration);
    }

    public void testExecuteWithMissingBuildFile() throws Exception {
        Project project = getProject();

        AntTargetConfiguration conf = new AntTargetConfiguration("missing", project.getBasePath() + "/missing/build.xml");
        command = (AddAntTargetCommand) conf.getCommand();
        try {
            command.execute(project);
            fail("Execute should throw an exception on missing build file");
        } catch (EclipserException ignored) {
        }
    }

    private AntRunConfiguration getAntRunConfiguration(String name) {
        AntRunConfiguration antRunConfiguration = null;
        RunManagerImpl manager = (RunManagerImpl) RunManager.getInstance(getProject());
        List<RunConfiguration> configurations = manager.getAllConfigurationsList();
        for (RunConfiguration runConfiguration : configurations) {
            if (runConfiguration.getName().equalsIgnoreCase(name)) {
                if (runConfiguration instanceof AntRunConfiguration) {
                    antRunConfiguration = (AntRunConfiguration) runConfiguration;
                }
            }
        }
        return antRunConfiguration;
    }

    @Override
    protected void tearDown() throws Exception {
        RunManagerImpl manager = (RunManagerImpl) RunManager.getInstance(getProject());
        manager.clearAll();
        Project project = getProject();
        AntConfiguration antConfiguration = AntConfiguration.getInstance(project);
        AntBuildFile[] antBuildFiles = antConfiguration.getBuildFiles();
        for (AntBuildFile antBuildFile : antBuildFiles) {
            antConfiguration.removeBuildFile(antBuildFile);
        }
        super.tearDown();
    }
}
