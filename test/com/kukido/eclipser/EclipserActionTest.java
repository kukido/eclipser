package com.kukido.eclipser;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.LightIdeaTestCase;
import com.intellij.testFramework.TestActionEvent;
import org.jetbrains.annotations.Nullable;

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

    public void testGetPsiElementsWithNull() {
        action = new EclipserAction();
        DataContext context = new DataContext() {
            @Nullable
            @Override
            public Object getData(String key) {
                return null;
            }
        };
        AnActionEvent event = new TestActionEvent(context, action);
        PsiElement[] elements = action.getPsiElements(event);
        assertEmpty(elements);
    }

    public void testGetPsiElementsWithPsiElementArray() throws IOException {
        action = new EclipserAction();
        final PsiFile[] psiFiles = {getPsiFile("valid.launch")};
        DataContext context = new DataContext() {
            @Nullable
            @Override
            public Object getData(String key) {
                if (LangDataKeys.PSI_ELEMENT_ARRAY.is(key)) {
                    return psiFiles;
                }
                return null;
            }
        };
        AnActionEvent event = new TestActionEvent(context, action);
        PsiElement[] elements = action.getPsiElements(event);
        assertEquals(1, elements.length);
    }

    public void testGetPsiElementsWithPsiFile() throws IOException {
        action = new EclipserAction();
        final PsiFile psiFile = getPsiFile("valid.launch");
        DataContext context = new DataContext() {
            @Nullable
            @Override
            public Object getData(String key) {
                if (LangDataKeys.PSI_FILE.is(key)) {
                    return psiFile;
                }
                return null;
            }
        };
        AnActionEvent event = new TestActionEvent(context, action);
        PsiElement[] elements = action.getPsiElements(event);
        assertEquals(1, elements.length);
    }

    private PsiFile getPsiFile(String name) throws IOException {
        return createFile(name, FileUtil.loadFile(new File(this.getClass().getResource("/resources/xml/" + name).getPath())));
    }
}
