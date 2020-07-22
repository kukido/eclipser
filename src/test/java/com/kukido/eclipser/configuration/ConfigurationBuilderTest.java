package com.kukido.eclipser.configuration;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.LightIdeaTestCase;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

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
        assertEquals(Collections.<String, String>emptyMap(), jc.getEnvironmentVariables());
    }

    public void testEnvConfiguration() throws Exception {
        PsiFile file = getPsiFile("env.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        assertInstanceOf(conf, JavaConfiguration.class);

        JavaConfiguration jc = (JavaConfiguration)conf;

        assertEquals("env", jc.getConfigurationName());
        assertEquals("Main", jc.getMainClassName());
        assertEquals("simple", jc.getModuleName());
        assertEquals(JavaConfiguration.MODULE_DIR_MACRO, jc.getWorkingDirectory());
        assertEquals("-Duser=${USER}", jc.getVmParameters());
        Map<String, String> expectedEnv = new LinkedHashMap<String, String>();
        expectedEnv.put("ENV","TEST");
        assertEquals(expectedEnv, jc.getEnvironmentVariables());
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
        assertEquals("--memory 10M --port 11111 --env ${ENV}", jc.getProgramParameters());
    }

    public void testJavaConfigurationWithArgumentsIncludingNewLine() throws Exception {
        PsiFile file = getPsiFile("arguments-withnewline.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        assertInstanceOf(conf, JavaConfiguration.class);

        JavaConfiguration jc = (JavaConfiguration)conf;

        assertEquals("arguments-withnewline", jc.getConfigurationName());
        assertEquals("com.thimbleware.jmemcached.Main", jc.getMainClassName());
        assertEquals("jmemcached-server", jc.getModuleName());
        assertEquals(JavaConfiguration.MODULE_DIR_MACRO, jc.getWorkingDirectory());
        assertEquals(String.format("--memory 10M%n--port 11111 --env ${ENV}"), jc.getProgramParameters());
    }

    public void testJavaConfigurationWithControlCharacters() throws Exception {
        PsiFile file = getPsiFile("newline.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        assertInstanceOf(conf, JavaConfiguration.class);

        JavaConfiguration jc = (JavaConfiguration) conf;

        assertEquals(String.format("-ea -Xmx512M%n-Dhbase.test=true"), jc.getVmParameters());
    }

    public void testJavaConfigurationWithWorkingDirectory() throws Exception {
        PsiFile file = getPsiFile("java-wd.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        assertInstanceOf(conf, JavaConfiguration.class);

        JavaConfiguration jc = (JavaConfiguration) conf;

        String workingDirectory = getProject().getBasePath() + "/";

        assertEquals(workingDirectory, jc.getWorkingDirectory());
    }

    public void testJavaConfigurationWithWorskspaceDefinedInVmParameters() throws Exception {
        PsiFile file = getPsiFile("workspace.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        assertInstanceOf(conf, JavaConfiguration.class);

        JavaConfiguration jc = (JavaConfiguration) conf;

        assertEquals("-Dhibernate.config.file=" + ExternalToolConfiguration.PROJECT_FILE_DIR + "/dbAccessLayer/resource/hibernate.cfg.xml", jc.getVmParameters());
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

    public void testAntTargetConfiguration() throws Exception {
        PsiFile file = getPsiFile("ant.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        AntTargetConfiguration at = (AntTargetConfiguration) conf;

        String expected = getProject().getBasePath() + "/developerPortal/build.xml";
        assertEquals(expected, at.getLocation());
        assertEquals("ant", at.getName());
    }

    public void testAntTargetConfigurationWithAbsolutePath() throws Exception {
        PsiFile file = getPsiFile("ant-github.launch");
        builder = new ConfigurationBuilder(file);
        Configuration conf = builder.build();

        AntTargetConfiguration at = (AntTargetConfiguration) conf;

        String expected = "/home/kukido/workspace/build.xml";
        assertEquals(expected, at.getLocation());
    }

    public void testRemoteApplicationConfiguration() throws Exception {
        PsiFile file = getPsiFile("remote.launch");
        builder = new ConfigurationBuilder(file);
        Configuration configuration = builder.build();

        RemoteJavaApplicationConfiguration rlc = (RemoteJavaApplicationConfiguration)configuration;
        assertEquals("localhost", rlc.getHostName());
        assertEquals("8000", rlc.getPort());
        assertEquals("remote", rlc.getName());
        assertEquals("org.eclipse.jdt.launching.socketAttachConnector", rlc.getVmConnectorId());
        assertEquals("spacebook", rlc.getModuleName());
    }

    @NotNull
    private PsiFile getPsiFile(String name) throws IOException {
        return createFile(name.replace(".launch", ".xml"), FileUtil.loadFile(new File(this.getClass().getClassLoader().getResource(name).getPath())));
    }
}
