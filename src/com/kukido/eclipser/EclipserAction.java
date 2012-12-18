package com.kukido.eclipser;

import com.intellij.execution.*;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EclipserAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        VirtualFile virtualFile = e.getData(LangDataKeys.VIRTUAL_FILE);

        try {
            load(virtualFile);
            run(e);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private void load(VirtualFile vf) throws IOException, JDOMException {

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
        }

        String type = root.getAttributeValue("type");
        System.out.println("type:" + type);

        // check for type
        // "org.eclipse.jdt.launching.localJavaApplication"
        // "org.eclipse.ant.AntLaunchConfigurationType"
        // "org.eclipse.jdt.junit.launchconfig"

        List content = root.getContent();

        for (Object item : content) {
            if (item instanceof Element) {
                Element element = (Element)item;
                String key = element.getAttributeValue("key");

                if (key.equalsIgnoreCase("org.eclipse.jdt.launching.MAIN_TYPE")) {
                    System.out.println("main:"+element.getAttributeValue("value"));
                } else if (key.equalsIgnoreCase("org.eclipse.jdt.launching.PROJECT_ATTR")) {
                    System.out.println("module:"+element.getAttributeValue("value"));
                } else if (key.equalsIgnoreCase("org.eclipse.jdt.launching.VM_ARGUMENTS")) {
                    System.out.println("vm args:"+element.getAttributeValue("value"));
                } else {
                    System.out.println(key + ":" + element.getAttributeValue("value"));
                }
            }
        }
    }

    private void run(AnActionEvent event) {

        Application application = ApplicationManager.getApplication();

        Project project = getEventProject(event);

        ApplicationConfiguration conf;
        // = new ApplicationConfiguration("developer portal", project, ApplicationConfigurationType.getInstance());

        RunManagerImpl runManager = (RunManagerImpl) RunManager.getInstance(project);

        RunnerAndConfigurationSettingsImpl runnerAndConfigurationSettings = findConfigurationByName("eclipser", runManager);

        if (runnerAndConfigurationSettings != null) {
            conf = (ApplicationConfiguration) runnerAndConfigurationSettings.getConfiguration();
        } else {
            EclipserConfigurationType type = application.getComponent(EclipserConfigurationType.class);
            runnerAndConfigurationSettings = (RunnerAndConfigurationSettingsImpl) runManager.createRunConfiguration("eclipser", type.getConfigurationFactories()[0]);
            conf = (ApplicationConfiguration) runnerAndConfigurationSettings.getConfiguration();
            //updateConfiguration(mainClassName, file, module, conf);
            runManager.addConfiguration(runnerAndConfigurationSettings, true);
        }

        String moduleNameOfRunner = "developerPortal";

        Module module = ModuleManager.getInstance(project).findModuleByName(moduleNameOfRunner);

        if (module == null) {
            say("Could not find the module of the runner with name '" + moduleNameOfRunner + "'. Check settings." +
                    "\n\nHere is the list of modules that were found:\n   " + StringUtils.join(ModuleManager.getInstance(project).getModules(), "\n   "));
            return;
        }

        conf.setModule(module);
        conf.setMainClassName("com.flurry.jetty.JettyServer");
        conf.setWorkingDirectory("$MODULE_DIR$");
        conf.setVMParameters("-ea -XX:MaxPermSize=128M -Xmx256M -DSHUTDOWN.PORT=23690 -Djetty.port=8087 -Dhibernate.config.file=\"../dbAccessLayer/resource/hibernate.cfg.xml\"");

        runManager.setSelectedConfiguration(runnerAndConfigurationSettings);

        Executor executor = DefaultRunExecutor.getRunExecutorInstance();
        ProgramRunner runner = RunnerRegistry.getInstance().getRunner(executor.getId(), conf);
        ExecutionEnvironment environment = new ExecutionEnvironment(runner, runnerAndConfigurationSettings, project);

        try {
            runner.execute(executor, environment);
        } catch (ExecutionException e1) {
            JavaExecutionUtil.showExecutionErrorMessage(e1, "Error", project);
        }

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
        super.update(e);
    }

    public void say(String message) {
        Messages.showMessageDialog(message, "Info", Messages.getInformationIcon());
    }
}
