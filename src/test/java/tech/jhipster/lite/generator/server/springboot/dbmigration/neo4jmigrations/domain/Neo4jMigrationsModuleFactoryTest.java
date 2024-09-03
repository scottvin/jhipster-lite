package tech.jhipster.lite.generator.server.springboot.dbmigration.neo4jmigrations.domain;

import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.assertThatModuleWithFiles;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.pomFile;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class Neo4jMigrationsModuleFactoryTest {

  private static final Neo4jMigrationsModuleFactory factory = new Neo4jMigrationsModuleFactory();

  @Test
  void shouldBuildModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest())
      .basePackage("tech.jhipster.jhlitest")
      .build();

    JHipsterModule module = factory.buildModule(properties);

    assertThatModuleWithFiles(module, pomFile())
      .hasFiles("documentation/neo4j-migrations.md")
      .hasFile("pom.xml")
      .containing(
        """
            <dependency>
              <groupId>eu.michael-simons.neo4j</groupId>
              <artifactId>neo4j-migrations-spring-boot-starter</artifactId>
              <version>${neo4j-migrations.version}</version>
            </dependency>
        """
      )
      .and()
      .hasFile("src/main/resources/config/application.yml")
      .containing(
        """
        org:
          neo4j:
            migrations:
              check-location: false
        """
      );
  }
}
