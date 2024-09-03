package tech.jhipster.lite.generator.server.springboot.docker.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;

import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.file.JHipsterSource;
import tech.jhipster.lite.module.domain.gradleplugin.GradleMainBuildPlugin;
import tech.jhipster.lite.module.domain.mavenplugin.MavenPlugin;
import tech.jhipster.lite.module.domain.mavenplugin.MavenPluginConfiguration;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.shared.error.domain.Assert;

public class SpringBootDockerModuleFactory {

  private static final String PROPERTIES_FIELD = "properties";
  private static final JHipsterSource SOURCE = from("server/springboot/docker");
  private static final JHipsterSource JIB_SOURCE = SOURCE.append("jib");
  private static final String JAVA_DOCKER_IMAGE = "eclipse-temurin:%s-jre-jammy";

  public JHipsterModule buildJibModule(JHipsterModuleProperties properties) {
    Assert.notNull(PROPERTIES_FIELD, properties);

    //@formatter:off
    return moduleBuilder(properties)
      .context()
        .put("mainClass", mainClassName(properties))
        .and()
      .mavenPlugins()
        .plugin(mavenJibPlugin(properties))
        .and()
      .gradlePlugins()
        .plugin(gradleJibPlugin(properties))
        .and()
      .files()
        .add(JIB_SOURCE.template("entrypoint.sh"), to("src/main/docker/jib").append("entrypoint.sh"))
        .and()
      .build();
    //@formatter:on
  }

  private String mainClassName(JHipsterModuleProperties properties) {
    return "%s.%sApp".formatted(properties.basePackage().get(), properties.projectBaseName().capitalized());
  }

  private MavenPlugin mavenJibPlugin(JHipsterModuleProperties properties) {
    return mavenPlugin()
      .groupId("com.google.cloud.tools")
      .artifactId("jib-maven-plugin")
      .versionSlug("jib-maven-plugin")
      .configuration(jibPluginConfiguration(properties))
      .build();
  }

  private String dockerBaseImage(JHipsterModuleProperties properties) {
    return JAVA_DOCKER_IMAGE.formatted(properties.javaVersion().get());
  }

  private MavenPluginConfiguration jibPluginConfiguration(JHipsterModuleProperties properties) {
    return new MavenPluginConfiguration(
      """
        <from>
          <image>%s</image>
          <platforms>
            <platform>
              <architecture>amd64</architecture>
              <os>linux</os>
            </platform>
          </platforms>
        </from>
        <to>
          <image>%s:latest</image>
        </to>
        <container>
          <entrypoint>
            <shell>bash</shell>
            <option>-c</option>
            <arg>/entrypoint.sh</arg>
          </entrypoint>
          <ports>
            <port>%s</port>
          </ports>
          <environment>
            <SPRING_OUTPUT_ANSI_ENABLED>ALWAYS</SPRING_OUTPUT_ANSI_ENABLED>
            <JHIPSTER_SLEEP>0</JHIPSTER_SLEEP>
          </environment>
          <creationTime>USE_CURRENT_TIMESTAMP</creationTime>
          <user>1000</user>
        </container>
        <extraDirectories>
          <paths>src/main/docker/jib</paths>
          <permissions>
            <permission>
              <file>/entrypoint.sh</file>
              <mode>755</mode>
            </permission>
          </permissions>
        </extraDirectories>
      """.formatted(dockerBaseImage(properties), properties.projectBaseName().get(), properties.serverPort().get())
    );
  }

  private GradleMainBuildPlugin gradleJibPlugin(JHipsterModuleProperties properties) {
    return gradleCommunityPlugin()
      .id("com.google.cloud.tools.jib")
      .pluginSlug("jib")
      .versionSlug("jib")
      .configuration(
        """
        jib {
          from {
            image = "%s"
            platforms {
              platform {
                architecture = "amd64"
                os = "linux"
              }
            }
          }
          to {
            image = "%s:latest"
          }
          container {
            entrypoint = listOf("bash", "-c", "/entrypoint.sh")
            ports = listOf("%s")
            environment = mapOf(
             "SPRING_OUTPUT_ANSI_ENABLED" to "ALWAYS",
             "JHIPSTER_SLEEP" to "0"
            )
            creationTime = "USE_CURRENT_TIMESTAMP"
            user = "1000"
          }
          extraDirectories {
            paths {
              path {
                setFrom("src/main/docker/jib")
              }
            }
            permissions = mapOf("/entrypoint.sh" to "755")
          }
        }""".formatted(dockerBaseImage(properties), properties.projectBaseName().get(), properties.serverPort().get())
      )
      .build();
  }

  public JHipsterModule buildDockerFileMavenModule(JHipsterModuleProperties properties) {
    Assert.notNull(PROPERTIES_FIELD, properties);

    return moduleBuilder(properties).files().add(SOURCE.template("Dockerfile-maven"), to("Dockerfile")).and().build();
  }

  public JHipsterModule buildDockerFileGradleModule(JHipsterModuleProperties properties) {
    Assert.notNull(PROPERTIES_FIELD, properties);

    return moduleBuilder(properties).files().add(SOURCE.template("Dockerfile-gradle"), to("Dockerfile")).and().build();
  }
}
