package tech.jhipster.lite.generator.server.springboot.cache.ehcache.domain;

import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class EHCacheModulesFactoryTest {

  private static final EHCacheModulesFactory factory = new EHCacheModulesFactory();

  @Test
  void shouldBuildJavaConfigurationModule() {
    JHipsterModule module = factory.buildJavaConfigurationModule(properties());

    commonEHCacheModuleAsserter(module)
      .hasFile("src/main/java/tech/jhipster/jhlitest/wire/cache/infrastructure/secondary/CacheConfiguration.java")
      .containing("JCacheManagerCustomizer")
      .and()
      .hasFiles("src/main/java/tech/jhipster/jhlitest/wire/cache/infrastructure/secondary/EhcacheProperties.java")
      .hasPrefixedFiles(
        "src/test/java/tech/jhipster/jhlitest/wire/cache/infrastructure/secondary",
        "CacheConfigurationIT.java",
        "CacheConfigurationTest.java"
      )
      .hasFile("src/main/resources/config/application.yml")
      .containing(
        """
        application:
          cache:
            ehcache:
              max-entries: 100
              time-to-live-seconds: 3600
        """
      );
  }

  @Test
  void shouldBuildXmlConfigurationModule() {
    JHipsterModule module = factory.buildXmlConfigurationModule(properties());

    commonEHCacheModuleAsserter(module)
      .hasFiles("src/main/resources/config/ehcache/ehcache.xml")
      .hasFile("src/main/resources/config/application.yml")
      .containing(
        """
        spring:
          cache:
            jcache:
              config: classpath:config/ehcache/ehcache.xml
        """
      );
  }

  private JHipsterModuleAsserter commonEHCacheModuleAsserter(JHipsterModule module) {
    return assertThatModuleWithFiles(module, pomFile(), logbackFile(), testLogbackFile())
      .hasFile("pom.xml")
      .containing(
        """
            <dependency>
              <groupId>javax.cache</groupId>
              <artifactId>cache-api</artifactId>
            </dependency>
        """
      )
      .containing(
        """
            <dependency>
              <groupId>org.ehcache</groupId>
              <artifactId>ehcache</artifactId>
              <classifier>jakarta</classifier>
            </dependency>
        """
      )
      .and()
      .hasFile("src/main/resources/logback-spring.xml")
      .containing("<logger name=\"org.ehcache\" level=\"WARN\" />")
      .and()
      .hasFile("src/test/resources/logback.xml")
      .containing("<logger name=\"org.ehcache\" level=\"WARN\" />")
      .and();
  }

  private JHipsterModuleProperties properties() {
    return JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest()).basePackage("tech.jhipster.jhlitest").build();
  }
}
