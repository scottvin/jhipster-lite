package tech.jhipster.lite.generator.server.springboot.cucumber.domain;

import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.assertThatModuleWithFiles;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.pomFile;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class CucumberModuleFactoryTest {

  private static final CucumberModuleFactory factory = new CucumberModuleFactory();

  @Test
  void shouldBuildInitialModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest())
      .basePackage("tech.jhipster.jhlitest")
      .projectBaseName("myapp")
      .build();

    JHipsterModule module = factory.buildInitializationModule(properties);

    assertThatModuleWithFiles(module, pomFile())
      .hasPrefixedFiles("src/test/java/tech/jhipster/jhlitest/cucumber", "CucumberConfiguration.java", "CucumberTest.java")
      .hasPrefixedFiles(
        "src/test/java/tech/jhipster/jhlitest/cucumber/rest",
        "AsyncElementAsserter.java",
        "AsyncHeaderAsserter.java",
        "AsyncResponseAsserter.java",
        "Awaiter.java",
        "CucumberRestAssertions.java",
        "CucumberRestTemplate.java",
        "CucumberJson.java",
        "CucumberRestTestContext.java",
        "CucumberRestTestContextUnitTest.java",
        "ElementAsserter.java",
        "ElementAssertions.java",
        "HeaderAsserter.java",
        "HeaderAssertions.java",
        "ResponseAsserter.java",
        "SyncElementAsserter.java",
        "SyncHeaderAsserter.java",
        "SyncResponseAsserter.java"
      )
      .hasFiles("documentation/cucumber.md")
      .hasFiles("src/test/features/.gitkeep")
      .hasFile("pom.xml")
      .containing("<artifactId>cucumber-junit-platform-engine</artifactId>")
      .containing("<artifactId>cucumber-java</artifactId>")
      .containing("<artifactId>cucumber-spring</artifactId>")
      .containing("<artifactId>junit-platform-suite</artifactId>")
      .containing("<version>${cucumber.version}</version>")
      .and()
      .doNotHaveFiles("src/test/java/tech/jhipster/jhlitest/cucumber/CucumberJpaReset.java");
  }

  @Test
  void shouldBuildJpaResetModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest())
      .basePackage("tech.jhipster.jhlitest")
      .build();

    JHipsterModule module = factory.buildJpaResetModule(properties);

    assertThatModuleWithFiles(module, pomFile()).hasFiles("src/test/java/tech/jhipster/jhlitest/cucumber/CucumberJpaReset.java");
  }
}
