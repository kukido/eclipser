package com.kukido.eclipser;

import com.intellij.execution.*;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.tools.Enabler;
import com.intellij.tools.Tool;
import com.intellij.tools.ToolManager;
import com.intellij.tools.ToolsGroup;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EclipserAction extends AnAction {

    public static final String MODULE_DIR = "$MODULE_DIR$";
    public static final String LOCAL_JAVA_APPLICATION = "org.eclipse.jdt.launching.localJavaApplication";

    public void actionPerformed(AnActionEvent e) {
        VirtualFile virtualFile = e.getData(LangDataKeys.VIRTUAL_FILE);

        try {
            EclipserConfiguration configuration = load(virtualFile);
            if (configuration != null)
                run(e, configuration);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private EclipserConfiguration load(VirtualFile vf) throws IOException, JDOMException {

        Document physical;

        final InputStream is = vf.getInputStream();
        try {
            physical = JDOMUtil.loadDocument(is);
        } finally {
            is.close();
        }

        Element root = physical.getRootElement();

        // do parsing here

        String name = vf.getNameWithoutExtension();
        System.out.println("name:" + name);

        // skip reading part and try to run hardcoded configuration

        String configuration = root.getName();
        if (!"launchConfiguration".equalsIgnoreCase(configuration)) {
            // show message - unsupported configuration type
            say("Unsupported launch configuration");
            return null;
        }

        String type = root.getAttributeValue("type");
        System.out.println("type:" + type);

        if (EclipserXml.CONFIGURATION_TYPE_PROGRAM_LAUNCH.equalsIgnoreCase(type)) {
            createExternalTool(null);
            return null;
        }

        if (!EclipserXml.CONFIGURATION_TYPE_LOCAL_JAVA_APPLICATION.equalsIgnoreCase(type)) {
            say("Unsupported launch configuration type:" + type);
            return null;
        }

        EclipserConfiguration eclipserConfiguration = new EclipserConfiguration();

        eclipserConfiguration.setConfigurationName(name);

        List content = root.getContent();

        for (Object item : content) {
            if (item instanceof Element) {
                Element element = (Element)item;
                String key = element.getAttributeValue(EclipserXml.KEY);

                if (key.equalsIgnoreCase(EclipserXml.MAIN_TYPE_KEY)) {
                    eclipserConfiguration.setMainClassName(element.getAttributeValue(EclipserXml.VALUE));
                } else if (key.equalsIgnoreCase(EclipserXml.PROJECT_ATTR_KEY)) {
                    eclipserConfiguration.setModuleName(element.getAttributeValue(EclipserXml.VALUE));
                } else if (key.equalsIgnoreCase(EclipserXml.VM_ARGUMENTS_KEY)) {
                    eclipserConfiguration.setVmParameters(element.getAttributeValue(EclipserXml.VALUE));
                } else {
                    System.out.println(key + ":" + element.getAttributeValue(EclipserXml.VALUE));
                }
            }
        }

        return eclipserConfiguration;
    }

    private void createExternalTool(AnActionEvent event) {

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

    private void run(AnActionEvent event, EclipserConfiguration configuration) {

        Application application = ApplicationManager.getApplication();

        Project project = getEventProject(event);

        ApplicationConfiguration conf;
        // = new ApplicationConfiguration("developer portal", project, ApplicationConfigurationType.getInstance());

        RunManagerImpl runManager = (RunManagerImpl) RunManager.getInstance(project);

        RunnerAndConfigurationSettingsImpl runnerAndConfigurationSettings = findConfigurationByName(configuration.getConfigurationName(), runManager);

        if (runnerAndConfigurationSettings != null) {
            conf = (ApplicationConfiguration) runnerAndConfigurationSettings.getConfiguration();
        } else {
            EclipserConfigurationType type = application.getComponent(EclipserConfigurationType.class);
            runnerAndConfigurationSettings = (RunnerAndConfigurationSettingsImpl) runManager.createRunConfiguration(configuration.getConfigurationName(), type.getConfigurationFactories()[0]);
            conf = (ApplicationConfiguration) runnerAndConfigurationSettings.getConfiguration();
            //updateConfiguration(mainClassName, file, module, conf);
            runManager.addConfiguration(runnerAndConfigurationSettings, true);
        }

        String moduleNameOfRunner = configuration.getModuleName();

        Module module = ModuleManager.getInstance(project).findModuleByName(moduleNameOfRunner);

        if (module == null) {
            say("Could not find the module of the runner with name '" + moduleNameOfRunner + "'. Check settings." +
                    "\n\nHere is the list of modules that were found:\n   " + StringUtils.join(ModuleManager.getInstance(project).getModules(), "\n   "));
            return;
        }

        conf.setModule(module);
        conf.setMainClassName(configuration.getMainClassName());
        conf.setWorkingDirectory(MODULE_DIR);
        conf.setVMParameters(configuration.getVmParameters());

        runManager.setSelectedConfiguration(runnerAndConfigurationSettings);

        /*
        Executor executor = DefaultRunExecutor.getRunExecutorInstance();
        ProgramRunner runner = RunnerRegistry.getInstance().getRunner(executor.getId(), conf);
        ExecutionEnvironment environment = new ExecutionEnvironment(runner, runnerAndConfigurationSettings, project);

        try {
            runner.execute(executor, environment);
        } catch (ExecutionException e1) {
            JavaExecutionUtil.showExecutionErrorMessage(e1, "Error", project);
        }
        */
    }

    private RunnerAndConfigurationSettingsImpl findConfigurationByName(String name, RunManagerImpl runManager){
        for (RunnerAndConfigurationSettings settings : runManager.getSortedConfigurations()){
            if (settings.getName().equals(name))
                return (RunnerAndConfigurationSettingsImpl) settings;
        }
        return null;

    }


    @Override
    public void update(AnActionEvent e) {

        final Presentation presentation = e.getPresentation();
        final Project project = e.getProject();

        if (project == null) {
            disable(presentation);
            return;
        }

        final VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        if (file == null) {
            disable(presentation);
            return;
        }

        final PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (psiFile == null) {
            disable(presentation);
            return;
        }

        if (!(psiFile instanceof XmlFile)) {
            disable(presentation);
            return;
        }

        final XmlFile xmlFile = (XmlFile) psiFile;
        final XmlDocument document = xmlFile.getDocument();
        if (document == null) {
            disable(presentation);
            return;
        }

        final XmlTag rootTag = xmlFile.getRootTag();
        if (rootTag == null) {
            disable(presentation);
            return;
        }

        if (!"launchConfiguration".equalsIgnoreCase(rootTag.getName())) {
            disable(presentation);
            return;
        }

        enable(presentation);
    }

    public void say(String message) {
        Messages.showMessageDialog(message, "Info", Messages.getInformationIcon());
    }

    private void disable(Presentation presentation) {
        presentation.setEnabledAndVisible(false);
    }

    private void enable(Presentation presentation) {
        presentation.setEnabledAndVisible(true);
    }

}
