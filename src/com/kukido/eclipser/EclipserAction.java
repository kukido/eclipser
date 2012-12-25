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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.kukido.eclipser.command.Command;
import com.kukido.eclipser.configuration.Configuration;
import com.kukido.eclipser.configuration.ConfigurationBuilder;
import org.apache.commons.lang.StringUtils;

public class EclipserAction extends AnAction {

    public static final String MODULE_DIR = "$MODULE_DIR$";

    public void actionPerformed(AnActionEvent e) {
        VirtualFile virtualFile = e.getData(LangDataKeys.VIRTUAL_FILE);
        Project project = e.getProject();

        try {
            ConfigurationBuilder builder = new ConfigurationBuilder(project, virtualFile);
            Configuration configuration = builder.build();

            if (configuration != null) {
                Command command = configuration.getCommand();
                command.execute();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
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
