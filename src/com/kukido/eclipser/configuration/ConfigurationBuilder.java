package com.kukido.eclipser.configuration;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.kukido.eclipser.EclipserXml;

public class ConfigurationBuilder {

    private PsiFile psiFile;

    private String configurationName;
    private String mainType;
    private String moduleName;
    private String workingDirectory;
    private String vmParameters;

    public ConfigurationBuilder(PsiFile psiFile) {
        this.psiFile = psiFile;
    }

    public Configuration build() {

        // read configuration type
        // based on the type create configuration

        if (!(psiFile instanceof XmlFile)) {
            // throw exception
        }

        XmlFile input = (XmlFile)psiFile;

        XmlTag root = input.getRootTag();

        String configurationType = root.getAttribute(EclipserXml.TYPE).getValue();

        PsiElement[] children = root.getChildren();

        for (PsiElement child : children) {
            System.out.println();
            if (child instanceof XmlTagImpl) {
                XmlTagImpl tag = (XmlTagImpl)child;
                String key = tag.getAttributeValue(EclipserXml.KEY);
                String name = tag.getName();
                if (EclipserXml.STRING_ATTRIBUTE.equalsIgnoreCase(name)) {
                    String value = tag.getAttributeValue(EclipserXml.VALUE);
                    if (EclipserXml.MAIN_TYPE_KEY.equalsIgnoreCase(key)) {
                        mainType = value;
                    } else if (EclipserXml.VM_ARGUMENTS_KEY.equalsIgnoreCase(key)) {
                        vmParameters = value;
                    } else if (EclipserXml.PROJECT_ATTR_KEY.equalsIgnoreCase(key)) {
                        moduleName = value;
                    }
                }
            }
        }

        if (EclipserXml.CONFIGURATION_TYPE_LOCAL_JAVA_APPLICATION.equalsIgnoreCase(configurationType)) {
            configurationName = psiFile.getVirtualFile().getNameWithoutExtension();
        }

        if (EclipserXml.CONFIGURATION_TYPE_LOCAL_JAVA_APPLICATION.equalsIgnoreCase(configurationType)) {
            return new JavaConfiguration(configurationName, mainType, moduleName, vmParameters);
        } else {
            // throw exception?
            return null;
        }
    }
}
