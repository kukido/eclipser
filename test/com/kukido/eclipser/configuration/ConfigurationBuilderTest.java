package com.kukido.eclipser.configuration;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.LightIdeaTestCase;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ConfigurationBuilderTest extends LightIdeaTestCase {

    private ConfigurationBuilder builder;

    @Test
    public void testWithJavaConfiguration() throws Exception {

        PsiFile file = getPsiFile("java.launch");

        builder = new ConfigurationBuilder(file);

        Configuration conf = builder.build();

        assertInstanceOf(conf, JavaConfiguration.class);

        JavaConfiguration jc = (JavaConfiguration)conf;

        assertEquals("java", jc.getConfigurationName());
        assertEquals("com.flurry.jetty.JettyServer", jc.getMainClassName());
        assertEquals("developerPortal", jc.getModuleName());
        assertEquals(JavaConfiguration.MODULE_DIR, jc.getWorkingDirectory());
        assertEquals("-ea -XX:MaxPermSize=128M -Xmx256M -DSHUTDOWN.PORT=\"28087\" -Djetty.port=\"8087\" -Dhibernate.config.file=\"../dbAccessLayer/resource/hibernate.cfg.xml\"", jc.getVmParameters());

    }

    @Test
    public void testWithExternalToolConfiguration() throws Exception {
        PsiFile file = getPsiFile("tool.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        assertInstanceOf(conf, ExternalToolConfiguration.class);

        ExternalToolConfiguration etc = (ExternalToolConfiguration)conf;

        assertEquals("tool", etc.getName());
        assertEquals("/kafka/kafka/config/zookeeper.properties", etc.getParameters());
        assertEquals("/kafka/kafka/bin/zookeeper", etc.getProgram());
        assertEquals(ExternalToolConfiguration.PROJECT_FILE_DIR, etc.getWorkingDirectory());
    }



    private PsiFile getPsiFile(String name) throws IOException {
        return createFile(name, FileUtil.loadFile(new File(this.getClass().getResource("/resources/" + name).getPath())));
    }


}
