package com.kukido.eclipser;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.kukido.eclipser.command.Command;
import com.kukido.eclipser.configuration.Configuration;
import com.kukido.eclipser.configuration.ConfigurationBuilder;
import com.kukido.eclipser.configuration.ConfigurationType;
import org.jetbrains.annotations.NotNull;

class EclipserAction extends AnAction {

    private static final String DEFAULT_FAILURE_MESSAGE = "%s\nEclipser was unable to convert launch file. Please submit a ticket at <a href='https://github.com/kukido/eclipser/issues'>eclipser/issues</a>";
    private static final String MESSAGE_FOR_UNSUPPORTED_LAUNCH_CONFIGURATION = "%s\nThe launch configuration is currently not supported by Eclipser.";
    private static final String MESSAGE_FOR_UNKNOWN_LAUNCH_CONFIGURATION = "%s\nThis is an unknown launch configuration. If you would like Eclipser to support it, please submit a ticket at <a href='https://github.com/kukido/eclipser/issues'>eclipser/issues</a>";

    public void actionPerformed(@NotNull AnActionEvent e) {

        final Project project = e.getProject();
        final PsiElement[] elements = getPsiElements(e);

        for (PsiElement element : elements) {
            process(element, project);
        }
    }

    private void process(PsiElement psiElement, Project project) {
        PsiFile psiFile = (PsiFile) psiElement;

        String message = null;

        try {
            ConfigurationType type = ConfigurationType.configurationTypeForPsiFile(psiFile);
            switch (type) {
                case UNSUPPORTED:
                    say(String.format(MESSAGE_FOR_UNSUPPORTED_LAUNCH_CONFIGURATION, psiFile.getName()));
                    return;
                case UNKNOWN:
                    say(String.format(MESSAGE_FOR_UNKNOWN_LAUNCH_CONFIGURATION, psiFile.getName()));
                    return;
                case SUPPORTED:
                    break;
            }

            ConfigurationBuilder builder = new ConfigurationBuilder(psiFile);
            Configuration configuration = builder.build();
            Command command = configuration.getCommand();
            command.execute(project);
        } catch (EclipserException ee) {
            message = ee.getMessage();
        } catch (Exception exc) {
            exc.printStackTrace();
            message = String.format(DEFAULT_FAILURE_MESSAGE, psiFile.getName());
        }

        if (message != null) say(message);
    }

    void say(String message) {
        Messages.showMessageDialog(message, "Info", Messages.getInformationIcon());
    }

    @Override
    public void update(@NotNull AnActionEvent e) {

        final Presentation presentation = e.getPresentation();
        final Project project = e.getProject();

        if (project == null) {
            disable(presentation);
            return;
        }

        final VirtualFile[] files = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);

        if (files == null) {
            disable(presentation);
            return;
        }

        for (VirtualFile file : files) {
            final PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
            if (!isSupported(psiFile)) {
                disable(presentation);
                return;
            }
        }

        enable(presentation);
    }

    boolean isSupported(PsiFile psiFile) {

        if (!(psiFile instanceof XmlFile)) {
            return false;
        }

        final XmlFile xmlFile = (XmlFile) psiFile;
        final XmlDocument document = xmlFile.getDocument();
        if (document == null) {
            return false;
        }

        final XmlTag rootTag = xmlFile.getRootTag();
        if (rootTag == null) {
            return false;
        }

        final XmlAttribute typeAttribute = rootTag.getAttribute(EclipserXml.TYPE);
        if (typeAttribute == null) {
            return false;
        }

        //noinspection RedundantIfStatement
        if (!"launchConfiguration".equalsIgnoreCase(rootTag.getName())) {
            return false;
        }

        return true;
    }

    PsiElement[] getPsiElements(AnActionEvent event) {

        PsiElement[] psiElements = event.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
        PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);

        if (psiFile == null && psiElements == null)
            return PsiElement.EMPTY_ARRAY;

        if (psiFile == null) {
            return psiElements;
        } else {
            return new PsiElement[]{psiFile};
        }
    }

    private void disable(Presentation presentation) {
        presentation.setEnabledAndVisible(false);
    }

    private void enable(Presentation presentation) {
        presentation.setEnabledAndVisible(true);
    }

}
