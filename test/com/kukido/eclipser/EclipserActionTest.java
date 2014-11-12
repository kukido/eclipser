package com.kukido.eclipser;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.LightIdeaTestCase;

import java.io.File;
import java.io.IOException;

public class EclipserActionTest extends LightIdeaTestCase {

    private EclipserAction action;

    public void testIsSupportedWithValidFile() throws IOException {
        action = new EclipserAction();
        PsiFile valid = getPsiFile("valid.launch");
        assertTrue(action.isSupported(valid));
    }

    public void testIsSupportedWithInvalidRootTag() throws IOException {
        action = new EclipserAction();
        PsiFile invalid = getPsiFile("invalidroottag.launch");
        assertFalse(action.isSupported(invalid));
    }

    public void testIsSupportedWithNoTypeAttribute() throws IOException {
        action = new EclipserAction();
        PsiFile invalid = getPsiFile("notypeattribute.launch");
        assertFalse(action.isSupported(invalid));
    }

    public void testIsSupportedWithNoRootTag() throws IOException {
        action = new EclipserAction();
        PsiFile invalid = getPsiFile("noroottag.launch");
        assertFalse(action.isSupported(invalid));
    }

    private PsiFile getPsiFile(String name) throws IOException {
        return createFile(name, FileUtil.loadFile(new File(this.getClass().getResource("/resources/xml/" + name).getPath())));
    }
}
