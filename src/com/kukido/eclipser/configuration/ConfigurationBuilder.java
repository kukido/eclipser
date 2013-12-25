package com.kukido.eclipser.configuration;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.EclipserXml;

public class ConfigurationBuilder {

    private PsiFile psiFile;

    private String name;
    private String mainType;
    private String moduleName;
    private String vmParameters;
    private String parameters;
    private String program;
    private String programArguments;
    private String workingDirectory;
    private boolean resolveToWorkspace;
    private String[] profiles;
    private String commandLine;

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
            if (child instanceof XmlTagImpl) {
                XmlTagImpl tag = (XmlTagImpl)child;
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
                        program = convertWorkspace(value);
                    } else if (EclipserXml.ATTR_TOOL_ARGUMENTS_KEY.equalsIgnoreCase(key)) {
                        parameters = convertWorkspace(value);
                    } else if (EclipserXml.PROGRAM_ARGUMENTS_KEY.equalsIgnoreCase(key)) {
                        programArguments = value;
                    } else if (EclipserXml.ATTR_WORKING_DIRECTORY_KEY.equalsIgnoreCase(key)) {
                        workingDirectory = convertWorkspace(value);
                    } else if (EclipserXml.M2_PROFILES_KEY.equalsIgnoreCase(key)) {
                        profiles = convertProfiles(value);
                    } else if (EclipserXml.M2_GOALS_KEY.equalsIgnoreCase(key)) {
                        commandLine = value;
                    }
                } else if (EclipserXml.BOOLEAN_ATTRIBUTE.equalsIgnoreCase(name)) {
                    boolean value = Boolean.valueOf(tag.getAttributeValue(EclipserXml.VALUE));
                    if (EclipserXml.M2_WORKSPACE_RESOLUTION.equalsIgnoreCase(key)) {
                        resolveToWorkspace = value;
                    }
                }
            }
        }

        if (EclipserXml.CONFIGURATION_TYPE_LOCAL_JAVA_APPLICATION.equalsIgnoreCase(configurationType) ||
            EclipserXml.CONFIGURATION_TYPE_PROGRAM_LAUNCH.equalsIgnoreCase(configurationType) ||
            EclipserXml.CONFIGURATION_TYPE_MAVEN2_LAUNCH.equalsIgnoreCase(configurationType)) {
            name = psiFile.getVirtualFile().getNameWithoutExtension();
        }

		return createConfiguration(configurationType);
	}

	private Configuration createConfiguration(String configurationType) throws EclipserException {
		if (EclipserXml.CONFIGURATION_TYPE_LOCAL_JAVA_APPLICATION.equalsIgnoreCase(configurationType)) {
			return new JavaConfiguration(name, mainType, moduleName, vmParameters, programArguments);
		} else if (EclipserXml.CONFIGURATION_TYPE_PROGRAM_LAUNCH.equalsIgnoreCase(configurationType)) {
			return new ExternalToolConfiguration(name, program, parameters, workingDirectory);
        } else if (EclipserXml.CONFIGURATION_TYPE_MAVEN2_LAUNCH.equalsIgnoreCase(configurationType)) {
            return new Maven2Configuration(name, resolveToWorkspace, profiles, commandLine);
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
        return value.replace("}", "").replace("${workspace_loc:", ExternalToolConfiguration.PROJECT_FILE_DIR);
    }
}
