package com.kukido.eclipser.configuration;

import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.kukido.eclipser.EclipserXml;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public enum ConfigurationType {

    SUPPORTED,
    UNSUPPORTED,
    UNKNOWN;

    private static final List<String> supportedConfigurationType = Arrays.asList(
            EclipserXml.CONFIGURATION_TYPE_ANT_LAUNCH,
            EclipserXml.CONFIGURATION_TYPE_LOCAL_JAVA_APPLICATION,
            EclipserXml.CONFIGURATION_TYPE_MAVEN2_LAUNCH,
            EclipserXml.CONFIGURATION_TYPE_PROGRAM_LAUNCH,
            EclipserXml.CONFIGURATION_TYPE_REMOTE_JAVA_APPLICATION
    );

    private static final List<String> unsupportedConfigurationType = Arrays.asList(
            EclipserXml.CONFIGURATION_TYPE_GWT_WEB_APPLICATION,
            EclipserXml.CONFIGURATION_TYPE_JUNIT_LAUNCH,
            EclipserXml.CONFIGURATION_TYPE_RUNTIME_WORKBENCH
    );

    public static ConfigurationType configurationTypeForPsiFile(PsiFile psiFile) throws Exception {
        String type = getConfigurationType(psiFile);
        if (supportedConfigurationType.contains(type)) {
            return SUPPORTED;
        } else if (unsupportedConfigurationType.contains(type)) {
            return UNSUPPORTED;
        } else {
            return UNKNOWN;
        }
    }

    @Contract("null -> fail")
    private static String getConfigurationType(PsiFile psiFile) throws Exception {
        assert psiFile instanceof XmlFile;

        XmlFile input = (XmlFile) psiFile;

        XmlTag root = input.getRootTag();

        //noinspection ConstantConditions
        return root.getAttribute(EclipserXml.TYPE).getValue();
    }

}
