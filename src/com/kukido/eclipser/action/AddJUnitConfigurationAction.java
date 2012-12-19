package com.kukido.eclipser.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: andrey
 * Date: 12/18/12
 * Time: 10:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddJUnitConfigurationAction {

    public void action(AnActionEvent event) {

        DataContext dataContext = event.getDataContext();
        Project project = PlatformDataKeys.PROJECT.getData(dataContext);
        VirtualFile file = PlatformDataKeys.VIRTUAL_FILE.getData(dataContext);






    }


}
