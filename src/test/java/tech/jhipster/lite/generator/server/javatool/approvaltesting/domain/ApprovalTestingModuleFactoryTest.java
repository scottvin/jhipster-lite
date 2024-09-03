package tech.jhipster.lite.generator.server.javatool.approvaltesting.domain;

import static tech.jhipster.lite.TestFileUtils.*;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class ApprovalTestingModuleFactoryTest {

  private final ApprovalTestingModuleFactory factory = new ApprovalTestingModuleFactory();

  @Test
  void shouldBuildApprovalTestsModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(tmpDirForTest()).build();

    JHipsterModule module = factory.build(properties);

    assertThatModuleWithFiles(module, pomFile())
      .hasFile("pom.xml")
      .containing(
        """
            <dependency>
              <groupId>com.approvaltests</groupId>
              <artifactId>approvaltests</artifactId>
              <version>${approvaltests.version}</version>
              <scope>test</scope>
            </dependency>
        """
      );
  }
}
