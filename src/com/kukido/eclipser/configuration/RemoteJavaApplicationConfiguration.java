package com.kukido.eclipser.configuration;

import com.kukido.eclipser.EclipserException;
import com.kukido.eclipser.command.AddRemoteJavaApplicationCommand;
import com.kukido.eclipser.command.Command;

import java.util.Map;

public class RemoteJavaApplicationConfiguration implements Configuration {

    public static final String HOST_NAME_KEY = "hostname";
    public static final String PORT_KEY = "port";

    private final String name;
    private final Map<String, String> connectMap;
    private final String vmConnectorId;
    private final String moduleName;

    public RemoteJavaApplicationConfiguration(String name, Map<String, String> connectMap, String vmConnectorId, String moduleName) {
        this.name = name;
        this.connectMap = connectMap;
        this.vmConnectorId = vmConnectorId;
        this.moduleName = moduleName;
    }

    public String getName() {
        return name;
    }

    public String getHostName() {
        return connectMap.get(HOST_NAME_KEY);
    }

    public String getPort() {
        return connectMap.get(PORT_KEY);
    }

    public String getVmConnectorId() {
        return vmConnectorId;
    }

    public String getModuleName() {
        return moduleName;
    }

    @Override
    public Command getCommand() throws EclipserException {
        return new AddRemoteJavaApplicationCommand(this);
    }
}
