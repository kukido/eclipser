package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class EclipserIcons {
    private static Icon load(String path) {
        return IconLoader.getIcon(path, EclipserIcons.class);
    }

    public static final Icon Launch = load("/images/eclipser.gif");

}
