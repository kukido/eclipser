package com.kukido.eclipser.configuration;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.EclipserXml;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class ConfigurationBuilder {

    private final PsiFile psiFile;
    private String name;
    private String mainType;
    private String moduleName;
    private String vmParameters;
    private String parameters;
    private String location;
    private String programArguments;
    private String attrWorkingDirectory;
    private String workingDirectory;
    private boolean resolveToWorkspace;
    private String[] profiles;
    private String commandLine;
    private Map<String, String> environmentVariables;
    private Map<String, String> connectMap;
    private String vmConnectorId;

    public ConfigurationBuilder(PsiFile psiFile) {
        this.psiFile = psiFile;
    }

    public Configuration build() throws EclipserException {

        assert psiFile instanceof XmlFile;

        XmlFile input = (XmlFile) psiFile;

        XmlTag root = input.getRootTag();

        @SuppressWarnings("ConstantConditions")
        String configurationType = root.getAttribute(EclipserXml.TYPE).getValue();

        PsiElement[] children = root.getChildren();

        for (PsiElement child : children) {
            if (child instanceof XmlTagImpl) {
                XmlTagImpl tag = (XmlTagImpl) child;
                String key = tag.getAttributeValue(EclipserXml.KEY);
                String name = tag.getName();
                if (EclipserXml.STRING_ATTRIBUTE.equalsIgnoreCase(name)) {
                    String value = tag.getAttributeValue(EclipserXml.VALUE);
                    if (EclipserXml.MAIN_TYPE_KEY.equalsIgnoreCase(key)) {
                        mainType = value;
                    } else if (EclipserXml.VM_ARGUMENTS_KEY.equalsIgnoreCase(key)) {
                        vmParameters = convertWorkspace(normalizeText(value));
                    } else if (EclipserXml.PROJECT_ATTR_KEY.equalsIgnoreCase(key)) {
                        moduleName = value;
                    } else if (EclipserXml.ATTR_LOCATION_KEY.equalsIgnoreCase(key)) {
                        location = value;
                    } else if (EclipserXml.ATTR_TOOL_ARGUMENTS_KEY.equalsIgnoreCase(key)) {
                        parameters = convertWorkspace(value);
                    } else if (EclipserXml.PROGRAM_ARGUMENTS_KEY.equalsIgnoreCase(key)) {
                        programArguments = value;
                    } else if (EclipserXml.ATTR_WORKING_DIRECTORY_KEY.equalsIgnoreCase(key)) {
                        attrWorkingDirectory = convertWorkspace(value);
                    } else if (EclipserXml.M2_PROFILES_KEY.equalsIgnoreCase(key)) {
                        profiles = convertProfiles(value);
                    } else if (EclipserXml.M2_GOALS_KEY.equalsIgnoreCase(key)) {
                        commandLine = value;
                    } else if (EclipserXml.WORKING_DIRECTORY_KEY.equalsIgnoreCase(key)) {
                        workingDirectory = value;
                    } else if (EclipserXml.VM_CONNECTOR_ID_KEY.equalsIgnoreCase(key)) {
                        vmConnectorId = value;
                    }
                } else if (EclipserXml.BOOLEAN_ATTRIBUTE.equalsIgnoreCase(name)) {
                    boolean value = Boolean.valueOf(tag.getAttributeValue(EclipserXml.VALUE));
                    if (EclipserXml.M2_WORKSPACE_RESOLUTION.equalsIgnoreCase(key)) {
                        resolveToWorkspace = value;
                    }
                } else if (EclipserXml.MAP_ATTRIBUTE.equalsIgnoreCase(name)) {
                    if (EclipserXml.ENVIRONMENT_VARIABLES_KEY.equalsIgnoreCase(key)) {
                        environmentVariables = getMap(tag.getChildren());
                    } else if (EclipserXml.CONNECT_MAP_KEY.equalsIgnoreCase(key)) {
                        connectMap = getMap(tag.getChildren());
                    }
                }
            }
        }

        if (useLaunchFileNameForConfigurationName(configurationType)) {
            name = psiFile.getVirtualFile().getNameWithoutExtension();
        }

        return createConfiguration(configurationType);
    }

    private Map<String, String> getMap(PsiElement[] entries) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        for (PsiElement entry : entries) {
            if (entry instanceof XmlTagImpl && EclipserXml.MAP_ENTRY_ATTRIBUTE.equalsIgnoreCase(((XmlTagImpl) entry).getName())) {
                XmlTagImpl tag = (XmlTagImpl) entry;
                result.put(tag.getAttributeValue(EclipserXml.KEY), tag.getAttributeValue(EclipserXml.VALUE));
            }
        }
        return result;
    }

    private Configuration createConfiguration(String configurationType) throws EclipserException {
        if (EclipserXml.CONFIGURATION_TYPE_LOCAL_JAVA_APPLICATION.equalsIgnoreCase(configurationType)) {
            return new JavaConfiguration(name, mainType, moduleName, vmParameters, programArguments, environmentVariables);
        } else if (EclipserXml.CONFIGURATION_TYPE_PROGRAM_LAUNCH.equalsIgnoreCase(configurationType)) {
            return new ExternalToolConfiguration(name, convertWorkspace(location), parameters, attrWorkingDirectory);
        } else if (EclipserXml.CONFIGURATION_TYPE_MAVEN2_LAUNCH.equalsIgnoreCase(configurationType)) {
            return new Maven2Configuration(name, resolveToWorkspace, profiles, commandLine, resolveToProjectLocation(workingDirectory));
        } else if (EclipserXml.CONFIGURATION_TYPE_ANT_LAUNCH.equalsIgnoreCase(configurationType)) {
            return new AntTargetConfiguration(name, resolveToProjectLocation(location));
        } else if (EclipserXml.CONFIGURATION_TYPE_REMOTE_JAVA_APPLICATION.equalsIgnoreCase(configurationType)) {
            return new RemoteJavaApplicationConfiguration(name, connectMap, vmConnectorId, moduleName);
        } else {
            throw new EclipserException("Unsupported configuration type: " + configurationType);
        }
    }

    private String normalizeText(String value) {
        return normalizeQuotes(normalizeControlCharacters(value));
    }

    private String normalizeQuotes(String value) {
        return value.replace("&quot;", "\"");
    }

    private String normalizeControlCharacters(String value) {
        String lineSeparator = String.format("%n");
        return value
                .replace("&#13;&#10;", lineSeparator)
                .replace("&#13;", lineSeparator)
                .replace("&#10;", lineSeparator);
    }

    private String[] convertProfiles(String value) {
        return value.split(",");
    }

    private String convertWorkspace(String value) {
        return replaceWorkspaceLoc(value, ExternalToolConfiguration.PROJECT_FILE_DIR);
    }

    private String replaceWorkspaceLoc(String value, String basePath) {
        return value.replaceAll("\\$\\{workspace_loc:([^\\}]*)\\}", Matcher.quoteReplacement(basePath) + "$1");
    }

    private String resolveToProjectLocation(String value) {
        if (value.contains(EclipserXml.PROJECT_LOC)) {
            return value.replace("${project_loc}", psiFile.getProject().getBasePath());
        } else if (value.contains(EclipserXml.WORKSPACE_LOC)) {
            return replaceWorkspaceLoc(value, psiFile.getProject().getBasePath());
        } else {
            return value;
        }
    }

    private boolean useLaunchFileNameForConfigurationName(String configurationType) {
        return EclipserXml.CONFIGURATION_TYPE_LOCAL_JAVA_APPLICATION.equalsIgnoreCase(configurationType) ||
                EclipserXml.CONFIGURATION_TYPE_PROGRAM_LAUNCH.equalsIgnoreCase(configurationType) ||
                EclipserXml.CONFIGURATION_TYPE_MAVEN2_LAUNCH.equalsIgnoreCase(configurationType) ||
                EclipserXml.CONFIGURATION_TYPE_ANT_LAUNCH.equalsIgnoreCase(configurationType) ||
                EclipserXml.CONFIGURATION_TYPE_REMOTE_JAVA_APPLICATION.equalsIgnoreCase(configurationType);
    }
}
