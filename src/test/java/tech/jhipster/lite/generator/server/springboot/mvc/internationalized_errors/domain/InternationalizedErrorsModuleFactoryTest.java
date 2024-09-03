package tech.jhipster.lite.generator.server.springboot.mvc.internationalized_errors.domain;

import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class InternationalizedErrorsModuleFactoryTest {

  private static final InternationalizedErrorsModuleFactory factory = new InternationalizedErrorsModuleFactory();

  @Test
  void shouldBuildModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest())
      .basePackage("tech.jhipster.jhlitest")
      .projectBaseName("jhipster")
      .build();

    JHipsterModule module = factory.buildModule(properties);

    assertThatModuleWithFiles(module, pomFile())
      .hasFile("pom.xml")
      .containing(
        """
            <dependency>
              <groupId>org.reflections</groupId>
              <artifactId>reflections</artifactId>
              <version>${reflections.version}</version>
              <scope>test</scope>
            </dependency>
        """
      )
      .and()
      .hasFiles("documentation/application-errors.md")
      .hasPrefixedFiles(
        "src/main/java/tech/jhipster/jhlitest/shared/error/domain",
        "ErrorStatus.java",
        "ErrorKey.java",
        "StandardErrorKey.java",
        "JhipsterException.java"
      )
      .hasPrefixedFiles(
        "src/main/java/tech/jhipster/jhlitest/shared/error/infrastructure/primary",
        "ArgumentsReplacer.java",
        "AssertionErrorsConfiguration.java",
        "AssertionErrorsHandler.java",
        "JhipsterErrorsConfiguration.java",
        "JhipsterErrorsHandler.java"
      )
      .hasPrefixedFiles(
        "src/main/resources/messages/assertions-errors",
        "assertion-errors-messages.properties",
        "assertion-errors-messages_fr.properties"
      )
      .hasPrefixedFiles(
        "src/main/resources/messages/errors",
        "jhipster-errors-messages.properties",
        "jhipster-errors-messages_fr.properties"
      )
      .hasPrefixedFiles(
        "src/test/java/tech/jhipster/jhlitest/shared/error/infrastructure/primary",
        "JhipsterErrorsHandlerIT.java",
        "JhipsterErrorsMessagesTest.java",
        "JhipsterExceptionFactory.java",
        "ArgumentsReplacerTest.java",
        "AssertionErrorMessagesTest.java",
        "AssertionErrorsHandlerIT.java",
        "AssertionErrorsHandlerTest.java"
      )
      .hasFile("src/test/java/tech/jhipster/jhlitest/shared/error/infrastructure/primary/JhipsterErrorsHandlerTest.java")
      .containing("handler.handleJhipsterException(JhipsterException.internalServerError")
      .and()
      .hasPrefixedFiles("src/test/java/tech/jhipster/jhlitest/shared/error/domain", "JhipsterExceptionTest.java", "ErrorKeyTest.java")
      .hasFiles("src/test/java/tech/jhipster/jhlitest/shared/error_generator/domain/NullElementInCollectionExceptionFactory.java")
      .hasPrefixedFiles(
        "src/test/java/tech/jhipster/jhlitest/shared/error_generator/infrastructure/primary",
        "AssertionsErrorsResource.java",
        "JhipsterErrorsResource.java"
      );
  }
}
