package com.intellij.tools;

public class Enabler {

    private String name;
    private boolean showInMainMenu;
    private boolean enabled;

    public Enabler(String name, boolean showInMainMenu, boolean enabled) {
        this.name = name;
        this.showInMainMenu = showInMainMenu;
        this.enabled = enabled;
    }

    public Tool create() {
        Tool tool = new Tool();
        tool.setName(name);
        tool.setShownInMainMenu(showInMainMenu);
        tool.setEnabled(enabled);
        return tool;
    }

}
