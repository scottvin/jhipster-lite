package tech.jhipster.lite.generator.server.springboot.core.domain;

import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class SpringBootCoreModuleFactoryTest {

  private static final SpringBootCoreModuleFactory factory = new SpringBootCoreModuleFactory();

  @Nested
  class Maven {

    @Test
    void shouldBuildModuleOnProjectWithoutDefaultGoal() {
      JHipsterModuleProperties properties = properties();

      JHipsterModule module = factory.buildModule(properties);

      assertThatModuleWithFiles(module, pomFile())
        .hasFile("pom.xml")
        .containing(
          """
                <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-dependencies</artifactId>
                  <version>${spring-boot.version}</version>
                  <type>pom</type>
                  <scope>import</scope>
                </dependency>
          """
        )
        .containing(
          """
              <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter</artifactId>
              </dependency>
          """
        )
        .containing(
          """
              <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-configuration-processor</artifactId>
                <optional>true</optional>
              </dependency>
          """
        )
        .containing(
          """
              <dependency>
                <groupId>org.apache.commons</groupId>
                <artifactId>commons-lang3</artifactId>
              </dependency>
          """
        )
        .containing(
          """
              <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
              </dependency>
          """
        )
        .containing(
          """
                  <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <version>${spring-boot.version}</version>
                    <executions>
                      <execution>
                        <goals>
                          <goal>repackage</goal>
                        </goals>
                      </execution>
                    </executions>
                    <configuration>
                      <mainClass>tech.jhipster.jhlitest.MyappApp</mainClass>
                    </configuration>
                  </plugin>
          """
        )
        .containing(
          """
                <plugin>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-maven-plugin</artifactId>
                </plugin>
          """
        )
        .notContaining(
          """
              <dependency>
                <groupId>org.junit.jupiter</groupId>
                <artifactId>junit-jupiter-engine</artifactId>
                <version>${junit-jupiter.version}</version>
                <scope>test</scope>
              </dependency>
          """
        )
        .notContaining(
          """
              <dependency>
                <groupId>org.junit.jupiter</groupId>
                <artifactId>junit-jupiter-params</artifactId>
                <version>${junit-jupiter.version}</version>
                <scope>test</scope>
              </dependency>
          """
        )
        .notContaining(
          """
              <dependency>
                <groupId>org.assertj</groupId>
                <artifactId>assertj-core</artifactId>
                <version>${assertj.version}</version>
                <scope>test</scope>
              </dependency>
          """
        )
        .notContaining(
          """
              <dependency>
                <groupId>org.mockito</groupId>
                <artifactId>mockito-junit-jupiter</artifactId>
                <version>${mockito.version}</version>
                <scope>test</scope>
              </dependency>
          """
        )
        .containing("    <defaultGoal>spring-boot:run</defaultGoal>")
        .and()
        .hasFile("src/main/java/tech/jhipster/jhlitest/MyappApp.java")
        .containing("class MyappApp")
        .and()
        .hasFiles("src/main/java/tech/jhipster/jhlitest/ApplicationStartupTraces.java")
        .hasPrefixedFiles("src/test/java/tech/jhipster/jhlitest", "ApplicationStartupTracesTest.java", "IntegrationTest.java")
        .hasFile("src/main/resources/config/application.yml")
        .containing(
          """
          logging:
            level:
              tech:
                jhipster:
                  jhlitest: INFO
          spring:
            application:
              name: Myapp
          """
        )
        .and()
        .hasFile("src/main/resources/config/application-local.yml")
        .containing(
          """
          logging:
            level:
              tech:
                jhipster:
                  jhlitest: DEBUG
          """
        )
        .and()
        .hasFile("src/test/resources/config/application-test.yml")
        .containing(
          """
          logging:
            config: classpath:logback.xml
            level:
              tech:
                jhipster:
                  jhlitest: 'OFF'
          spring:
            main:
              banner-mode: 'off'
          """
        )
        .and()
        .hasFiles("src/test/resources/logback.xml", "src/main/resources/logback-spring.xml");
    }

    @Test
    void shouldBuildModuleOnProjectWithDefaultGoal() {
      JHipsterModuleProperties properties = properties();

      JHipsterModule module = factory.buildModule(properties);

      assertThatModuleWithFiles(module, pomWithDefaultGoal())
        .hasFile("pom.xml")
        .containing("<defaultGoal>dummy</defaultGoal>")
        .notContaining("<defaultGoal>spring-boot:run</defaultGoal>");
    }

    private ModuleFile pomWithDefaultGoal() {
      return file("src/test/resources/projects/maven-with-default-goal/pom.xml", "pom.xml");
    }
  }

  @Nested
  class Gradle {

    @Test
    void shouldBuildModule() {
      JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest())
        .basePackage("tech.jhipster.jhlitest")
        .projectBaseName("myapp")
        .put("serverPort", 9000)
        .build();

      JHipsterModule module = factory.buildModule(properties);

      assertThatModuleWithFiles(module, gradleBuildFile(), gradleLibsVersionFile())
        .hasFile("gradle/libs.versions.toml")
        .containing("spring-boot = \"")
        .containing(
          """
          \t[plugins.spring-boot]
          \t\tid = "org.springframework.boot"

          \t\t[plugins.spring-boot.version]
          \t\t\tref = "spring-boot"
          """
        )
        .and()
        .hasFile("build.gradle.kts")
        .containing(
          """
            alias(libs.plugins.spring.boot)
            // jhipster-needle-gradle-plugins
          """
        )
        .containing("defaultTasks(\"bootRun\")")
        .containing(
          """
          springBoot {
            mainClass = "tech.jhipster.jhlitest.MyappApp"
          }
          """
        )
        .containing("testImplementation(libs.spring.boot.starter.test)")
        .notContaining("testImplementation(libs.junit.engine)")
        .notContaining("testImplementation(libs.junit.params)")
        .notContaining("testImplementation(libs.assertj)")
        .notContaining("testImplementation(libs.mockito)");
    }
  }

  private JHipsterModuleProperties properties() {
    return JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest())
      .basePackage("tech.jhipster.jhlitest")
      .projectBaseName("myapp")
      .build();
  }
}
