package com.kukido.eclipser.configuration;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.EclipserXml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigurationBuilder {

    private PsiFile psiFile;

    private String name;
    private String mainType;
    private String moduleName;
    private String vmParameters;
    private String parameters;
    private String program;

    public ConfigurationBuilder(PsiFile psiFile) {
        this.psiFile = psiFile;
    }

    public Configuration build() throws EclipserException {

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
                        vmParameters = normalizeQuotes(value);
                    } else if (EclipserXml.PROJECT_ATTR_KEY.equalsIgnoreCase(key)) {
                        moduleName = value;
                    } else if (EclipserXml.ATTR_LOCATION_KEY.equalsIgnoreCase(key)) {
                        program = extractText(value);
                    } else if (EclipserXml.ATTR_TOOL_ARGUMENTS_KEY.equalsIgnoreCase(key)) {
                        parameters = extractText(value);
                    }
                }
            }
        }

        if (EclipserXml.CONFIGURATION_TYPE_LOCAL_JAVA_APPLICATION.equalsIgnoreCase(configurationType) ||
                EclipserXml.CONFIGURATION_TYPE_PROGRAM_LAUNCH.equalsIgnoreCase(configurationType)) {
            name = psiFile.getVirtualFile().getNameWithoutExtension();
        }

        if (EclipserXml.CONFIGURATION_TYPE_LOCAL_JAVA_APPLICATION.equalsIgnoreCase(configurationType)) {
            return new JavaConfiguration(name, mainType, moduleName, vmParameters);
        } else if (EclipserXml.CONFIGURATION_TYPE_PROGRAM_LAUNCH.equalsIgnoreCase(configurationType)) {
            return new ExternalToolConfiguration(name, program, parameters);
        } else {
            throw new EclipserException("Unsupported configuration type: " + configurationType);
        }
    }

    private String extractText(String value) {
        Pattern pattern = Pattern.compile("([a-zA-Z_]*):([a-zA_Z_/.]*)");
        Matcher matcher = pattern.matcher(value);
        matcher.find();
        return matcher.group(2);
    }

    private String normalizeQuotes(String value) {
        return value.replace("&quot;", "\"");
    }



}
