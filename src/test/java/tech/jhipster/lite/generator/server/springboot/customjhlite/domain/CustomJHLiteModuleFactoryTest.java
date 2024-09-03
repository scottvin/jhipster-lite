package tech.jhipster.lite.generator.server.springboot.customjhlite.domain;

import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class CustomJHLiteModuleFactoryTest {

  private static final CustomJHLiteModuleFactory factory = new CustomJHLiteModuleFactory();

  @Test
  void shouldBuildModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest())
      .basePackage("tech.jhipster.jhlitest")
      .projectBaseName("myapp")
      .put("serverPort", 9000)
      .build();

    JHipsterModule module = factory.buildModule(properties);

    //@formatter:off
    assertThatModuleWithFiles(module, pomFile(), mainAppFile())
      .hasExecutableFiles("tests-ci/generate.sh", "tests-ci/start.sh", "tests-ci/stop.sh")
      .hasFile("tests-ci/generate.sh")
        .containing("http://localhost:9000")
        .and()
      .hasFile("tests-ci/start.sh")
        .containing("9000")
        .and()
      .hasFile("tests-ci/modulePayload.json")
        .containing("""
          "packageName": "tech.jhipster.jhlitest.APP_NAME",
        """)
        .and()
      .hasFile("pom.xml")
        .containing("<artifactId>cucumber-junit-platform-engine</artifactId>")
        .containing("<artifactId>cucumber-java</artifactId>")
        .containing("<artifactId>cucumber-spring</artifactId>")
        .containing("<artifactId>junit-platform-suite</artifactId>")
        .containing(
          """
              <dependency>
                <groupId>tech.jhipster.lite</groupId>
                <artifactId>jhlite</artifactId>
                <version>${jhlite.version}</version>
              </dependency>
          """
        )
        .containing(
          """
              <dependency>
                <groupId>tech.jhipster.lite</groupId>
                <artifactId>jhlite</artifactId>
                <version>${jhlite.version}</version>
                <type>test-jar</type>
                <classifier>tests</classifier>
                <scope>test</scope>
              </dependency>
          """
        )
        .and()
      .hasFile("src/main/resources/config/application.yml")
        .containing(
          """
          jhlite-hidden-resources:
            # Disable the modules and its dependencies by slugs
            slugs: custom-jhlite
          server:
            port: 9000
          spring:
            jackson:
              default-property-inclusion: non_null
          """
        )
        .and()
      .hasFile("src/test/resources/config/application-test.yml")
        .containing(
          """
          server:
            port: 0
          spring:
            main:
              allow-bean-definition-overriding: true
          """)
        .and()
      .hasFile("src/main/java/tech/jhipster/jhlitest/MyappApp.java")
        .containing("import tech.jhipster.lite.JHLiteApp;")
        .containing("@SpringBootApplication(scanBasePackageClasses = { JHLiteApp.class, MyappApp.class })")
        .and()
      .hasPrefixedFiles("documentation", "module-creation.md", "cucumber.md")
      .doNotHaveFiles(
        "src/main/java/tech/jhipster/test/security/infrastructure/primary/CorsFilterConfiguration.java",
        "src/main/java/tech/jhipster/test/security/infrastructure/primary/CorsProperties.java",
        "src/test/java/tech/jhipster/test/security/infrastructure/primary/CorsFilterConfigurationIT.java"
      )
      .hasFile("src/test/java/tech/jhipster/jhlitest/cucumber/CucumberTest.java")
        .containing("key = GLUE_PROPERTY_NAME, value = \"tech.jhipster.jhlitest, tech.jhipster.lite.module.infrastructure.primary\"")
        .and()
      .hasFile("src/test/java/tech/jhipster/jhlitest/cucumber/CucumberConfiguration.java")
        .containing("import tech.jhipster.jhlitest.MyappApp;")
        .and()
      .hasPrefixedFiles("src/main/java/tech/jhipster/jhlitest/shared/slug", "package-info.java", "domain/MyappFeatureSlug.java", "domain/MyappModuleSlug.java")
      .hasFiles("src/test/java/tech/jhipster/jhlitest/cucumber/rest/CucumberRestTemplate.java")
      .hasFiles("src/test/features/.gitkeep");
    //@formatter:on
  }

  private ModuleFile mainAppFile() {
    return file("src/test/resources/projects/files/MainApp.java", "src/main/java/tech/jhipster/jhlitest/MyappApp.java");
  }
}
