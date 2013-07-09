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
    public void testJavaConfiguration() throws Exception {
        PsiFile file = getPsiFile("java.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        assertInstanceOf(conf, JavaConfiguration.class);

        JavaConfiguration jc = (JavaConfiguration)conf;

        assertEquals("java", jc.getConfigurationName());
        assertEquals("com.example.jetty.JettyServer", jc.getMainClassName());
        assertEquals("developerPortal", jc.getModuleName());
        assertEquals(JavaConfiguration.MODULE_DIR, jc.getWorkingDirectory());
        assertEquals("-ea -XX:MaxPermSize=128M -Xmx256M -DSHUTDOWN.PORT=\"28087\" -Djetty.port=\"8087\" -Dhibernate.config.file=\"../dbAccessLayer/resource/hibernate.cfg.xml\"", jc.getVmParameters());
    }

    @Test
    public void testExternalToolConfiguration() throws Exception {
        PsiFile file = getPsiFile("tool.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        assertInstanceOf(conf, ExternalToolConfiguration.class);

        ExternalToolConfiguration etc = (ExternalToolConfiguration)conf;

        assertEquals("tool", etc.getName());
        assertEquals(ExternalToolConfiguration.PROJECT_FILE_DIR+"/kafka/kafka/config/zookeeper.properties", etc.getParameters());
        assertEquals(ExternalToolConfiguration.PROJECT_FILE_DIR+"/kafka/kafka/bin/zookeeper-server-start.sh", etc.getProgram());
        assertEquals(ExternalToolConfiguration.PROJECT_FILE_DIR, etc.getWorkingDirectory());
    }

    @Test
    public void testExternalToolConfigurationWithWorkingDirectory() throws Exception {
        PsiFile file = getPsiFile("directory.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        assertInstanceOf(conf, ExternalToolConfiguration.class);

        ExternalToolConfiguration etc = (ExternalToolConfiguration)conf;

        assertEquals("directory", etc.getName());
        assertEquals(ExternalToolConfiguration.PROJECT_FILE_DIR+"/ms_api/hbase/bin/start-local-hbase.sh", etc.getProgram());
        assertEquals(ExternalToolConfiguration.PROJECT_FILE_DIR+"/ms_api/hbase", etc.getWorkingDirectory());
    }

    @Test
    public void testJavaConfigurationWithArguments() throws Exception {
        PsiFile file = getPsiFile("arguments.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        assertInstanceOf(conf, JavaConfiguration.class);

        JavaConfiguration jc = (JavaConfiguration)conf;

        assertEquals("arguments", jc.getConfigurationName());
        assertEquals("com.thimbleware.jmemcached.Main", jc.getMainClassName());
        assertEquals("jmemcached-server", jc.getModuleName());
        assertEquals(JavaConfiguration.MODULE_DIR, jc.getWorkingDirectory());
        assertEquals("--memory 10M --port 11111", jc.getProgramParameters());
    }

	@Test
	public void testJavaConfigurationWithControlCharacters() throws Exception {
		PsiFile file = getPsiFile("newline.launch");
		builder = new ConfigurationBuilder(file);
		Configuration conf = builder.build();

		assertInstanceOf(conf, JavaConfiguration.class);

		JavaConfiguration jc = (JavaConfiguration)conf;

		assertEquals(String.format("-ea -Xmx512M%n-Dhbase.test=true"), jc.getVmParameters());
	}

    private PsiFile getPsiFile(String name) throws IOException {
        return createFile(name, FileUtil.loadFile(new File(this.getClass().getResource("/resources/" + name).getPath())));
    }

}
