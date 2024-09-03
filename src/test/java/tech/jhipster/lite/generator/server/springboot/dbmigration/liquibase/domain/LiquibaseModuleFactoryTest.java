package tech.jhipster.lite.generator.server.springboot.dbmigration.liquibase.domain;

import static tech.jhipster.lite.TestFileUtils.*;
import static tech.jhipster.lite.module.domain.properties.SpringConfigurationFormat.*;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class LiquibaseModuleFactoryTest {

  private static final LiquibaseModuleFactory factory = new LiquibaseModuleFactory();

  @Nested
  class LiquibaseModule {

    @Test
    void shouldBuildModule() {
      JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(tmpDirForTest())
        .basePackage("tech.jhipster.jhlitest")
        .build();

      JHipsterModule module = factory.buildModule(properties);

      assertThatModuleWithFiles(module, pomFile(), logbackFile(), testLogbackFile())
        .hasFile("pom.xml")
        .containing(
          """
              <dependency>
                <groupId>org.liquibase</groupId>
                <artifactId>liquibase-core</artifactId>
                <version>${liquibase.version}</version>
              </dependency>
          """
        )
        .and()
        .hasPrefixedFiles("src/main/resources/config/liquibase", "master.xml", "changelog/0000000000_example.xml")
        .hasFile("src/test/resources/logback.xml")
        .containing("<logger name=\"liquibase\" level=\"WARN\" />")
        .containing("<logger name=\"LiquibaseSchemaResolver\" level=\"INFO\" />")
        .containing("<logger name=\"com.zaxxer.hikari\" level=\"WARN\" />")
        .and()
        .hasFile("src/main/resources/logback-spring.xml")
        .containing("<logger name=\"liquibase\" level=\"WARN\" />")
        .containing("<logger name=\"LiquibaseSchemaResolver\" level=\"INFO\" />")
        .containing("<logger name=\"com.zaxxer.hikari\" level=\"WARN\" />")
        .and()
        .hasFile("src/main/resources/config/application.yml")
        .containing(
          """
          spring:
            liquibase:
              change-log: classpath:config/liquibase/master.xml
          """
        );
    }
  }

  @Nested
  class AsyncLiquibaseModule {

    @Test
    void shouldBuildAsyncModule() {
      JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(tmpDirForTest())
        .basePackage("tech.jhipster.jhlitest")
        .build();

      JHipsterModule module = factory.buildAsyncModule(properties);

      assertThatModuleWithFiles(module, pomFile(), logbackFile(), testLogbackFile())
        .hasPrefixedFiles(
          "src/main/java/tech/jhipster/jhlitest/wire/liquibase/infrastructure/secondary",
          "AsyncSpringLiquibase.java",
          "LiquibaseConfiguration.java",
          "SpringLiquibaseUtil.java"
        )
        .hasPrefixedFiles(
          "src/test/java/tech/jhipster/jhlitest/wire/liquibase/infrastructure/secondary",
          "AsyncSpringLiquibaseTest.java",
          "LiquibaseConfigurationIT.java",
          "SpringLiquibaseUtilTest.java"
        );
    }

    @Test
    void shouldBuildModuleWithYamlSpringConfigurationFormat() {
      JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(tmpDirForTest())
        .basePackage("tech.jhipster.jhlitest")
        .springConfigurationFormat(YAML)
        .build();

      JHipsterModule module = factory.buildAsyncModule(properties);

      assertThatModuleWithFiles(module, pomFile(), logbackFile(), testLogbackFile())
        .hasFile("src/test/java/tech/jhipster/jhlitest/wire/liquibase/infrastructure/secondary/SpringLiquibaseUtilTest.java")
        .containing("YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();")
        .notContaining("Properties properties = new Properties();");
    }

    @Test
    void shouldBuildModuleWithPropertiesSpringConfigurationFormat() {
      JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(tmpDirForTest())
        .basePackage("tech.jhipster.jhlitest")
        .springConfigurationFormat(PROPERTIES)
        .build();

      JHipsterModule module = factory.buildAsyncModule(properties);

      assertThatModuleWithFiles(module, pomFile(), logbackFile(), testLogbackFile())
        .hasFile("src/test/java/tech/jhipster/jhlitest/wire/liquibase/infrastructure/secondary/SpringLiquibaseUtilTest.java")
        .containing("Properties properties = new Properties();")
        .notContaining("YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();");
    }
  }
}
