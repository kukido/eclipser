package com.kukido.eclipser.configuration;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.LightIdeaTestCase;

import java.io.File;
import java.io.IOException;

public class ConfigurationBuilderTest extends LightIdeaTestCase {

    private ConfigurationBuilder builder;

    public void testJavaConfiguration() throws Exception {
        PsiFile file = getPsiFile("java.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        assertInstanceOf(conf, JavaConfiguration.class);

        JavaConfiguration jc = (JavaConfiguration)conf;

        assertEquals("java", jc.getConfigurationName());
        assertEquals("com.example.jetty.JettyServer", jc.getMainClassName());
        assertEquals("developerPortal", jc.getModuleName());
        assertEquals(JavaConfiguration.MODULE_DIR_MACRO, jc.getWorkingDirectory());
        assertEquals("-ea -XX:MaxPermSize=128M -Xmx256M -DSHUTDOWN.PORT=\"28087\" -Djetty.port=\"8087\" -Dhibernate.config.file=\"../dbAccessLayer/resource/hibernate.cfg.xml\"", jc.getVmParameters());
    }

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

    public void testJavaConfigurationWithArguments() throws Exception {
        PsiFile file = getPsiFile("arguments.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        assertInstanceOf(conf, JavaConfiguration.class);

        JavaConfiguration jc = (JavaConfiguration)conf;

        assertEquals("arguments", jc.getConfigurationName());
        assertEquals("com.thimbleware.jmemcached.Main", jc.getMainClassName());
        assertEquals("jmemcached-server", jc.getModuleName());
        assertEquals(JavaConfiguration.MODULE_DIR_MACRO, jc.getWorkingDirectory());
        assertEquals("--memory 10M --port 11111", jc.getProgramParameters());
    }

	public void testJavaConfigurationWithControlCharacters() throws Exception {
		PsiFile file = getPsiFile("newline.launch");
		builder = new ConfigurationBuilder(file);
		Configuration conf = builder.build();

		assertInstanceOf(conf, JavaConfiguration.class);

		JavaConfiguration jc = (JavaConfiguration)conf;

		assertEquals(String.format("-ea -Xmx512M%n-Dhbase.test=true"), jc.getVmParameters());
	}

	public void testJavaConfigurationWithWorskspaceDefinedInVmParameters() throws Exception {
		PsiFile file = getPsiFile("workspace.launch");
		builder = new ConfigurationBuilder(file);
		Configuration conf = builder.build();

		assertInstanceOf(conf, JavaConfiguration.class);

		JavaConfiguration jc = (JavaConfiguration)conf;

		assertEquals("-Dhibernate.config.file="+ExternalToolConfiguration.PROJECT_FILE_DIR+"/dbAccessLayer/resource/hibernate.cfg.xml", jc.getVmParameters());
	}

    public void testMavenConfiguration() throws Exception {
        PsiFile file = getPsiFile("maven.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        assertInstanceOf(conf, Maven2Configuration.class);

        Maven2Configuration mc = (Maven2Configuration)conf;

        assertEquals("maven", mc.getConfigurationName());
        assertEquals("clean install -DskipTests=true", mc.getCommandLine());
        String workingDirectory = getProject().getBasePath() + "/";
        assertEquals(workingDirectory, mc.getWorkingDirectory());
    }

    public void testMavenConfigurationWithProfiles() throws Exception {
        PsiFile file = getPsiFile("maven.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        assertInstanceOf(conf, Maven2Configuration.class);

        Maven2Configuration mc = (Maven2Configuration)conf;

        assertEquals(2, mc.getProfiles().length);
        assertEquals("localConfig", mc.getProfiles()[0]);
        assertEquals("dependencies", mc.getProfiles()[1]);
    }

    public void testMavenConfigurationWithResolveToWorkspace() throws Exception {
        PsiFile file = getPsiFile("resolve.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        assertInstanceOf(conf, Maven2Configuration.class);

        Maven2Configuration mc = (Maven2Configuration)conf;

        assertTrue(mc.isResolveToWorkspace());
    }

    public void testMavenConfigurationWithAbsolutePath() throws Exception {
        PsiFile file = getPsiFile("maven-absolute.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        Maven2Configuration mc = (Maven2Configuration)conf;

        assertEquals("/home/test/eclipser", mc.getWorkingDirectory());
    }

    public void testMavenConfigurationWithRelativePath() throws Exception {
        PsiFile file = getPsiFile("maven-relative.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        Maven2Configuration mc = (Maven2Configuration)conf;

        String expected = getProject().getBasePath() + "/foobar";

        assertEquals(expected, mc.getWorkingDirectory());
    }

    public void testMavenConfigurationWithProjectLocation() throws Exception {
        PsiFile file = getPsiFile("maven-project-loc.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        Maven2Configuration mc = (Maven2Configuration)conf;

        String expected = getProject().getBasePath() + "/foobar/";

        assertEquals(expected, mc.getWorkingDirectory());
    }

    private PsiFile getPsiFile(String name) throws IOException {
        return createFile(name, FileUtil.loadFile(new File(this.getClass().getResource("/resources/" + name).getPath())));
    }
}
