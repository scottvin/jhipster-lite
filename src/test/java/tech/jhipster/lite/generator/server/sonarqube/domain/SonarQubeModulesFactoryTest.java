package tech.jhipster.lite.generator.server.sonarqube.domain;

import static org.mockito.Mockito.*;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.docker.DockerImageVersion;
import tech.jhipster.lite.module.domain.docker.DockerImages;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
@ExtendWith(MockitoExtension.class)
class SonarQubeModulesFactoryTest {

  @Mock
  private DockerImages dockerImages;

  @InjectMocks
  private SonarQubeModulesFactory factory;

  @Nested
  class Maven {

    @Test
    void shouldBuildBackendModule() {
      mockSonarqubeImage();

      JHipsterModule module = factory.buildBackendModule(properties());

      assertCommonModule(module)
        .hasFile("sonar-project.properties")
        .containing("sonar.coverage.jacoco.xmlReportPaths=target/jacoco/jacoco.xml")
        .containing("sonar.junit.reportPaths=target/surefire-reports,target/failsafe-reports")
        .containing(
          "sonar.exclusions=src/main/webapp/main.ts, src/main/webapp/app/main.ts, src/main/webapp/content/**/*.*, target/classes/static/**/*.*, src/main/webapp/app/index.tsx"
        )
        .notContaining("sonar.testExecutionReportPaths=target/test-results/TESTS-results-sonar.xml");
    }

    @Test
    void shouldBuildBackendFrontendModule() {
      mockSonarqubeImage();

      JHipsterModule module = factory.buildBackendFrontendModule(properties());

      assertCommonModule(module)
        .hasFile("sonar-project.properties")
        .containing(
          "sonar.exclusions=src/main/webapp/main.ts, src/main/webapp/app/main.ts, src/main/webapp/content/**/*.*, target/classes/static/**/*.*, src/main/webapp/app/index.tsx"
        )
        .containing("sonar.testExecutionReportPaths=target/test-results/TESTS-results-sonar.xml")
        .containing("sonar.javascript.lcov.reportPaths=target/test-results/lcov.info");
    }

    private JHipsterModuleAsserter assertCommonModule(JHipsterModule module) {
      return assertThatModuleWithFiles(module, pomFile(), readmeFile())
        .hasFile("pom.xml")
        .containing(
          // language=xml
          """
                <plugin>
                  <groupId>org.codehaus.mojo</groupId>
                  <artifactId>properties-maven-plugin</artifactId>
                </plugin>
          """
        )
        .containing(
          // language=xml
          """
                  <plugin>
                    <groupId>org.codehaus.mojo</groupId>
                    <artifactId>properties-maven-plugin</artifactId>
                    <version>${properties-maven-plugin.version}</version>
                    <executions>
                      <execution>
                        <id>default-cli</id>
                        <phase>initialize</phase>
                        <goals>
                          <goal>read-project-properties</goal>
                        </goals>
                        <configuration>
                          <files>
                            <file>sonar-project.properties</file>
                          </files>
                        </configuration>
                      </execution>
                    </executions>
                  </plugin>
          """
        )
        .containing(
          """
                  <plugin>
                    <groupId>org.sonarsource.scanner.maven</groupId>
                    <artifactId>sonar-maven-plugin</artifactId>
                    <version>${sonar-maven-plugin.version}</version>
                  </plugin>
          """
        )
        .and()
        .hasFile("src/main/docker/sonar.yml")
        .containing("sonarqube:1.1.1")
        .and()
        .hasFile("documentation/sonar.md")
        .containing("docker compose -f src/main/docker/sonar.yml up -d")
        .containing("./gradlew clean build sonar --info")
        .and()
        .hasFile("documentation/sonar.md")
        .containing("docker compose -f src/main/docker/sonar.yml up -d")
        .containing("./mvnw clean verify sonar:sonar")
        .and()
        .hasFile("README.md")
        .and();
    }
  }

  @Nested
  class Gradle {

    @Test
    void shouldBuildBackendModule() {
      mockSonarqubeImage();

      JHipsterModule module = factory.buildBackendModule(properties());

      assertCommonModule(module)
        .hasFile("sonar-project.properties")
        .containing(
          "sonar.exclusions=src/main/webapp/main.ts, src/main/webapp/app/main.ts, src/main/webapp/content/**/*.*, build/classes/static/**/*.*, src/main/webapp/app/index.tsx"
        )
        .notContaining("sonar.testExecutionReportPaths=build/test-results/TESTS-results-sonar.xml");
    }

    @Test
    void shouldBuildBackendFrontendModule() {
      mockSonarqubeImage();

      JHipsterModule module = factory.buildBackendFrontendModule(properties());

      assertCommonModule(module)
        .hasFile("sonar-project.properties")
        .containing(
          "sonar.exclusions=src/main/webapp/main.ts, src/main/webapp/app/main.ts, src/main/webapp/content/**/*.*, build/classes/static/**/*.*, src/main/webapp/app/index.tsx"
        )
        .containing("sonar.testExecutionReportPaths=build/test-results/TESTS-results-sonar.xml")
        .containing("sonar.javascript.lcov.reportPaths=build/test-results/lcov.info");
    }

    private JHipsterModuleAsserter assertCommonModule(JHipsterModule module) {
      return assertThatModuleWithFiles(module, gradleBuildFile(), gradleLibsVersionFile(), readmeFile())
        .hasFile("gradle/libs.versions.toml")
        .containing("sonarqube = \"")
        .containing(
          """
          \t[plugins.sonarqube]
          \t\tid = "org.sonarqube"

          \t\t[plugins.sonarqube.version]
          \t\t\tref = "sonarqube"
          """
        )
        .and()
        .hasFile("build.gradle.kts")
        .containing(
          """
          import java.util.Properties
          // jhipster-needle-gradle-imports\
          """
        )
        .containing(
          """
            alias(libs.plugins.sonarqube)
            // jhipster-needle-gradle-plugins
          """
        )
        .containing(
          """
          val sonarProperties = Properties()
          File("sonar-project.properties").inputStream().use { inputStream ->
              sonarProperties.load(inputStream)
          }

          sonarqube {
              properties {
                sonarProperties
                  .map { it -> it.key as String to (it.value as String).split(",").map { it.trim() } }
                  .forEach { (key, values) -> property(key, values) }
                property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
                property("sonar.junit.reportPaths", "build/test-results/test,build/test-results/integrationTest")
              }
          }
          """
        )
        .and()
        .hasFile("documentation/sonar.md")
        .containing("docker compose -f src/main/docker/sonar.yml up -d")
        .containing("./gradlew clean build sonar --info")
        .and()
        .hasFile("README.md")
        .and();
    }
  }

  private void mockSonarqubeImage() {
    when(dockerImages.get("sonarqube")).thenReturn(new DockerImageVersion("sonarqube", "1.1.1"));
  }

  private JHipsterModuleProperties properties() {
    return JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest())
      .basePackage("tech.jhipster.jhlitest")
      .projectBaseName("myapp")
      .build();
  }
}
