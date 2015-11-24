package com.kukido.eclipser.configuration;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.LightIdeaTestCase;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class ConfigurationTypeTest extends LightIdeaTestCase {

    public void testSupportedConfigurationType() throws Exception {
        PsiFile file = getPsiFile("ant.launch");
        ConfigurationType type = ConfigurationType.configurationTypeForPsiFile(file);
        assertEquals(ConfigurationType.SUPPORTED, type);
    }

    public void testUnsupportedConfigurationType() throws Exception {
        PsiFile file = getPsiFile("workbench.launch");
        ConfigurationType type = ConfigurationType.configurationTypeForPsiFile(file);
        assertEquals(ConfigurationType.UNSUPPORTED, type);
    }

    public void testUnknownConfigurationType() throws Exception {
        PsiFile file = getPsiFile("unknown.launch");
        ConfigurationType type = ConfigurationType.configurationTypeForPsiFile(file);
        assertEquals(ConfigurationType.UNKNOWN, type);
    }

    @NotNull
    private PsiFile getPsiFile(String name) throws IOException {
        return createFile(name, FileUtil.loadFile(new File(this.getClass().getResource("/resources/" + name).getPath())));
    }
}
