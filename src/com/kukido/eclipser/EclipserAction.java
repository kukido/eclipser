package com.kukido.eclipser;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.IOException;
import java.io.InputStream;

public class EclipserAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here

        VirtualFile virtualFile = e.getData(LangDataKeys.VIRTUAL_FILE);

        try {
            load(virtualFile);
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        System.out.println("action!");
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

        System.out.println("element!");

        // skip reading part and try to run hardcoded configuration

    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
    }
}
