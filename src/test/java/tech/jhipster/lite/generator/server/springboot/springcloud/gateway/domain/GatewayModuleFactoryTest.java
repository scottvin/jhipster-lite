package tech.jhipster.lite.generator.server.springboot.springcloud.gateway.domain;

import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.assertThatModuleWithFiles;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.pomFile;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class GatewayModuleFactoryTest {

  private static final GatewayModuleFactory factory = new GatewayModuleFactory();

  @Test
  void shouldBuildModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest())
      .basePackage("tech.jhipster.jhlitest")
      .projectBaseName("myApp")
      .build();

    JHipsterModule module = factory.buildModule(properties);

    assertThatModuleWithFiles(module, pomFile())
      .hasFile("pom.xml")
      .containing("<spring-cloud.version>")
      .containing(
        """
              <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
              </dependency>
        """
      )
      .containing(
        """
            <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-bootstrap</artifactId>
            </dependency>
        """
      )
      .containing(
        """
            <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-gateway</artifactId>
            </dependency>
        """
      )
      .and()
      .hasFile("src/main/resources/config/bootstrap.yml")
      .containing(
        // language=yaml
        """
        spring:
          application:
            name: myApp
          cloud:
            gateway:
              discovery:
                locator:
                  enabled: true
                  filters[0]:
                    args[regexp]: '''/services/'' + serviceId.toLowerCase() + ''/(?<remaining>.*)'''
                    args[replacement]: '''/${remaining}'''
                    name: RewritePath
                  lower-case-service-id: true
                  predicates[0]:
                    args[pattern]: '''/services/''+serviceId.toLowerCase()+''/**'''
                    name: Path
        """
      )
      .and()
      .hasFile("src/test/resources/config/bootstrap.yml")
      .containing(
        // language=yaml
        """
        spring:
          application:
            name: myApp
          cloud:
            gateway:
              discovery:
                locator:
                  enabled: false
        """
      )
      .and()
      .hasJavaSources(
        "tech/jhipster/jhlitest/wire/gateway/infrastructure/primary/GatewayResource.java",
        "tech/jhipster/jhlitest/wire/gateway/infrastructure/primary/vm/RouteVM.java"
      )
      .hasJavaTests("tech/jhipster/jhlitest/wire/gateway/infrastructure/primary/GatewayResourceIT.java");
  }
}
