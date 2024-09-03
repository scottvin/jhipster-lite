package tech.jhipster.lite.generator.server.springboot.database.hibernate2ndlevelcache;

import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.assertThatModuleWithFiles;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.pomFile;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.generator.server.springboot.database.hibernate2ndlevelcache.domain.Hibernate2ndLevelCacheModuleFactory;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class Hibernate2ndLevelCacheModuleFactoryTest {

  private static final Hibernate2ndLevelCacheModuleFactory factory = new Hibernate2ndLevelCacheModuleFactory();

  @Test
  void shouldCreateModule() {
    JHipsterModule module = factory.buildModule(properties());

    assertThatModuleWithFiles(module, pomFile())
      .hasFile("pom.xml")
      .containing(
        """
            <dependency>
              <groupId>org.hibernate.orm</groupId>
              <artifactId>hibernate-jcache</artifactId>
            </dependency>
        """
      )
      .and()
      .hasFile("src/main/resources/config/application.yml")
      .containing(
        """
        spring:
          jpa:
            properties:
              hibernate:
                cache:
                  use_second_level_cache: true
        """
      )
      .and()
      .hasFile("src/test/java/tech/jhipster/jhlitest/wire/cache/infrastructure/secondary/Hibernate2ndLevelCacheConfigurationIT.java");
  }

  private JHipsterModuleProperties properties() {
    return JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest()).basePackage("tech.jhipster.jhlitest").build();
  }
}
