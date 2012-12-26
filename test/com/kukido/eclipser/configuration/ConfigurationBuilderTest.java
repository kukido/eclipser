package com.kukido.eclipser.configuration;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.LightIdeaTestCase;
import org.junit.Test;

import java.io.File;

public class ConfigurationBuilderTest extends LightIdeaTestCase {

    private ConfigurationBuilder builder;

    @Test
    public void testWithJavaConfiguration() throws Exception {

        PsiFile file = createFile("java.launch", FileUtil.loadFile(new File(this.getClass().getResource("/resources/java.launch").getPath())));

        builder = new ConfigurationBuilder(file);

        Configuration conf = builder.build();

        assertInstanceOf(conf, JavaConfiguration.class);

        JavaConfiguration jc = (JavaConfiguration)conf;

        assertEquals("java", jc.getConfigurationName());
        assertEquals("com.flurry.jetty.JettyServer", jc.getMainClassName());
        assertEquals("developerPortal", jc.getModuleName());
        assertEquals(JavaConfiguration.MODULE_DIR, jc.getWorkingDirectory());
        assertEquals("-ea -XX:MaxPermSize=128M -Xmx256M -DSHUTDOWN.PORT=&quot;28087&quot; -Djetty.port=&quot;8087&quot; -Dhibernate.config.file=&quot;../dbAccessLayer/resource/hibernate.cfg.xml&quot;", jc.getVmParameters());

    }
}
