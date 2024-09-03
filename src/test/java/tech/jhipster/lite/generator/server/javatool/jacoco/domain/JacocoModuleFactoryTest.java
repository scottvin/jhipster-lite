package tech.jhipster.lite.generator.server.javatool.jacoco.domain;

import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class JacocoModuleFactoryTest {

  private static final JacocoModuleFactory factory = new JacocoModuleFactory();

  @Nested
  class Maven {

    @Test
    void shouldBuildJacocoModule() {
      JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest()).build();

      JHipsterModule module = factory.buildJacocoModule(properties);

      assertThatModuleWithFiles(module, pomFile())
        .hasFile("pom.xml")
        .containing(
          """
                <plugin>
                  <groupId>org.jacoco</groupId>
                  <artifactId>jacoco-maven-plugin</artifactId>
                </plugin>
          """
        )
        .containing(
          """
                  <plugin>
                    <groupId>org.jacoco</groupId>
                    <artifactId>jacoco-maven-plugin</artifactId>
                    <version>${jacoco.version}</version>
                    <executions>
                      <execution>
                        <id>pre-unit-tests</id>
                        <goals>
                          <goal>prepare-agent</goal>
                        </goals>
                      </execution>
                      <execution>
                        <id>post-unit-test</id>
                        <phase>test</phase>
                        <goals>
                          <goal>report</goal>
                        </goals>
                      </execution>
                      <execution>
                        <id>pre-integration-tests</id>
                        <goals>
                          <goal>prepare-agent-integration</goal>
                        </goals>
                      </execution>
                      <execution>
                        <id>post-integration-tests</id>
                        <phase>post-integration-test</phase>
                        <goals>
                          <goal>report-integration</goal>
                        </goals>
                      </execution>
                      <execution>
                        <id>merge</id>
                        <phase>verify</phase>
                        <goals>
                          <goal>merge</goal>
                        </goals>
                        <configuration>
                          <fileSets>
                            <fileSet implementation="org.apache.maven.shared.model.fileset.FileSet">
                              <directory>${project.basedir}</directory>
                              <includes>
                                <include>**/*.exec</include>
                              </includes>
                            </fileSet>
                          </fileSets>
                          <destFile>target/jacoco/allTest.exec</destFile>
                        </configuration>
                      </execution>
                      <execution>
                        <id>post-merge-report</id>
                        <phase>verify</phase>
                        <goals>
                          <goal>report</goal>
                        </goals>
                        <configuration>
                          <dataFile>target/jacoco/allTest.exec</dataFile>
                          <outputDirectory>target/jacoco/</outputDirectory>
                        </configuration>
                      </execution>
                    </executions>
                  </plugin>
          """
        );
    }

    @Test
    void shouldBuildJacocoWithMinCoverageCheckModule() {
      JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest()).build();

      JHipsterModule module = factory.buildJacocoWithMinCoverageCheckModule(properties);

      assertThatModuleWithFiles(module, pomFile())
        .hasFile("pom.xml")
        .containing(
          """
                      <execution>
                        <id>check</id>
                        <goals>
                          <goal>check</goal>
                        </goals>
                        <configuration>
                          <dataFile>target/jacoco/allTest.exec</dataFile>
                          <rules>
                            <rule>
                              <element>CLASS</element>
                              <limits>
                                <limit>
                                  <counter>LINE</counter>
                                  <value>COVEREDRATIO</value>
                                  <minimum>1.00</minimum>
                                </limit>
                                <limit>
                                  <counter>BRANCH</counter>
                                  <value>COVEREDRATIO</value>
                                  <minimum>1.00</minimum>
                                </limit>
                              </limits>
                            </rule>
                          </rules>
                        </configuration>
                      </execution>
                    </executions>
                  </plugin>
          """
        );
    }
  }

  @Nested
  class Gradle {

    @Test
    void shouldBuildJacocoModule() {
      JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest()).build();

      JHipsterModule module = factory.buildJacocoModule(properties);

      assertThatModuleWithFiles(module, gradleBuildFile())
        .hasFile("build.gradle.kts")
        .containing(
          """
            jacoco
            // jhipster-needle-gradle-plugins
          """
        )
        .containing(
          """
          jacoco {
            toolVersion = libs.versions.jacoco.get()
          }

          tasks.jacocoTestReport {
            dependsOn("test", "integrationTest")
            reports {
              xml.required.set(true)
              html.required.set(true)
            }
            executionData.setFrom(fileTree(layout.buildDirectory).include("**/jacoco/test.exec", "**/jacoco/integrationTest.exec"))
          }
          """
        )
        .containing(
          """
            finalizedBy("jacocoTestReport")
            // jhipster-needle-gradle-tasks-test
          """
        )
        .and()
        .hasFile("gradle/libs.versions.toml")
        .containing("jacoco = \"");
    }
  }

  @Test
  void shouldBuildJacocoWithMinCoverageCheckModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest()).build();

    JHipsterModule module = factory.buildJacocoWithMinCoverageCheckModule(properties);

    assertThatModuleWithFiles(module, gradleBuildFile())
      .hasFile("build.gradle.kts")
      .containing(
        """
          jacoco
          // jhipster-needle-gradle-plugins
        """
      )
      .containing(
        """
        jacoco {
          toolVersion = libs.versions.jacoco.get()
        }

        tasks.jacocoTestReport {
          dependsOn("test", "integrationTest")
          reports {
            xml.required.set(true)
            html.required.set(true)
          }
          executionData.setFrom(fileTree(layout.buildDirectory).include("**/jacoco/test.exec", "**/jacoco/integrationTest.exec"))
        }

        tasks.jacocoTestCoverageVerification {
          dependsOn("jacocoTestReport")
          violationRules {

              rule {
                  element = "CLASS"

                  limit {
                      counter = "LINE"
                      value = "COVEREDRATIO"
                      minimum = "1.00".toBigDecimal()
                  }

                  limit {
                      counter = "BRANCH"
                      value = "COVEREDRATIO"
                      minimum = "1.00".toBigDecimal()
                  }
              }
          }
          executionData.setFrom(fileTree(layout.buildDirectory).include("**/jacoco/test.exec", "**/jacoco/integrationTest.exec"))
        }
        """
      )
      .containing(
        """
          finalizedBy("jacocoTestCoverageVerification")
          // jhipster-needle-gradle-tasks-test
        """
      )
      .and()
      .hasFile("gradle/libs.versions.toml")
      .containing("jacoco = \"");
  }
}
