package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class EclipserIcons {

    public static final String ECLIPSER_ICON_PATH = "/images/eclipser.gif";

    private static Icon load() {
        return IconLoader.getIcon(ECLIPSER_ICON_PATH, EclipserIcons.class);
    }

    public static final Icon Launch = load();

}
