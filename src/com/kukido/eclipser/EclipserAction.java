package com.kukido.eclipser;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.kukido.eclipser.command.Command;
import com.kukido.eclipser.configuration.Configuration;
import com.kukido.eclipser.configuration.ConfigurationBuilder;

public class EclipserAction extends AnAction {

    public static final String DEFAULT_FAILURE_MESSAGE = "Eclipser was unable to convert launch file. Please submit support ticket at http://bitbucket.org/kukido/eclipser/issues";

    public void actionPerformed(AnActionEvent e) {

        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);

        String message = null;

        try {
            ConfigurationBuilder builder = new ConfigurationBuilder(psiFile);
            Configuration configuration = builder.build();

            if (configuration != null) {
                Command command = configuration.getCommand();
                command.execute(e.getProject());
            }

        } catch (EclipserException ee) {
            message = ee.getMessage();
        } catch (Exception exc) {
            exc.printStackTrace();
            message = DEFAULT_FAILURE_MESSAGE;
        }

        if (message != null) say(message);
    }

    public void say(String message) {
        Messages.showMessageDialog(message, "Info", Messages.getInformationIcon());
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

		final XmlAttribute typeAttribute = rootTag.getAttribute(EclipserXml.TYPE);
		if (typeAttribute == null) {
			disable(presentation);
			return;
		}

        if (!"launchConfiguration".equalsIgnoreCase(rootTag.getName())) {
            disable(presentation);
            return;
        }

        enable(presentation);
    }

    private void disable(Presentation presentation) {
        presentation.setEnabledAndVisible(false);
    }

    private void enable(Presentation presentation) {
        presentation.setEnabledAndVisible(true);
    }

}
