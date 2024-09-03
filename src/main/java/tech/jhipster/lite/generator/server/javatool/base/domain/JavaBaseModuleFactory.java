package tech.jhipster.lite.generator.server.javatool.base.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.documentationTitle;
import static tech.jhipster.lite.module.domain.JHipsterModule.from;
import static tech.jhipster.lite.module.domain.JHipsterModule.moduleBuilder;
import static tech.jhipster.lite.module.domain.JHipsterModule.toSrcMainJava;
import static tech.jhipster.lite.module.domain.JHipsterModule.toSrcTestJava;

import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.file.JHipsterDestination;
import tech.jhipster.lite.module.domain.file.JHipsterSource;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.shared.error.domain.Assert;

public class JavaBaseModuleFactory {

  private static final String PACKAGE_INFO = "package-info.java";

  private static final String ERROR = "error";
  private static final String GENERATION = "generation";
  private static final String COLLECTION = "collection";

  private static final JHipsterSource SOURCE = from("server/javatool/base");
  private static final JHipsterSource MAIN_SOURCE = SOURCE.append("main");
  private static final JHipsterSource MAIN_GENERATION_SOURCE = MAIN_SOURCE.append(GENERATION);
  private static final JHipsterSource TEST_SOURCE = SOURCE.append("test");

  private enum Destination {
    COLLECTION("shared/collection"),
    COLLECTION_DOMAIN("shared/collection/domain"),
    GENERATION("shared/generation"),
    GENERATION_DOMAIN("shared/generation/domain"),
    ERROR("shared/error"),
    ERROR_DOMAIN("shared/error/domain");

    private final String path;

    Destination(String path) {
      this.path = path;
    }

    String path() {
      return this.path;
    }
  }

  public JHipsterModule buildModule(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    String packagePath = properties.packagePath();
    String baseName = properties.projectBaseName().capitalized();
    String basePackage = properties.basePackage().get();

    JHipsterDestination testDestination = toSrcTestJava().append(packagePath);
    JHipsterDestination mainDestination = toSrcMainJava().append(packagePath);

    //@formatter:off
    return moduleBuilder(properties)
      .context()
        .put("baseName", baseName)
        .put("basePackage", basePackage)
        .and()
      .documentation(documentationTitle("Package types"), MAIN_SOURCE.template("package-types.md"))
      .documentation(documentationTitle("Assertions"), MAIN_SOURCE.template("assertions.md"))
      .files()
        .batch(MAIN_SOURCE, mainDestination)
          .addTemplate("BusinessContext.java")
          .addTemplate("SharedKernel.java")
          .and()
        .batch(MAIN_SOURCE.append(ERROR), mainDestination.append(Destination.ERROR_DOMAIN.path()))
          .addTemplate("Assert.java")
          .addTemplate("AssertionErrorType.java")
          .addTemplate("MissingMandatoryValueException.java")
          .addTemplate("AssertionException.java")
          .addTemplate("NotAfterTimeException.java")
          .addTemplate("NotBeforeTimeException.java")
          .addTemplate("NullElementInCollectionException.java")
          .addTemplate("NumberValueTooHighException.java")
          .addTemplate("NumberValueTooLowException.java")
          .addTemplate("StringTooLongException.java")
          .addTemplate("StringTooShortException.java")
          .addTemplate("TooManyElementsException.java")
          .and()
        .batch(TEST_SOURCE.append(ERROR), testDestination.append(Destination.ERROR_DOMAIN.path()))
          .addTemplate("AssertTest.java")
          .addTemplate("MissingMandatoryValueExceptionTest.java")
          .addTemplate("NotAfterTimeExceptionTest.java")
          .addTemplate("NotBeforeTimeExceptionTest.java")
          .addTemplate("NullElementInCollectionExceptionTest.java")
          .addTemplate("NumberValueTooHighExceptionTest.java")
          .addTemplate("NumberValueTooLowExceptionTest.java")
          .addTemplate("StringTooLongExceptionTest.java")
          .addTemplate("StringTooShortExceptionTest.java")
          .addTemplate("TooManyElementsExceptionTest.java")
          .and()
        .batch(TEST_SOURCE, testDestination)
          .addTemplate("UnitTest.java")
          .addTemplate("ComponentTest.java")
          .addTemplate("ReplaceCamelCase.java")
          .and()
        .add(MAIN_SOURCE.append(ERROR).template(PACKAGE_INFO), packageInfoDestination(mainDestination, Destination.ERROR))
        .add(MAIN_GENERATION_SOURCE.template("ExcludeFromGeneratedCodeCoverage.java"), mainDestination.append(Destination.GENERATION_DOMAIN.path).append("ExcludeFromGeneratedCodeCoverage.java"))
        .add(MAIN_GENERATION_SOURCE.template(PACKAGE_INFO), mainDestination.append(Destination.GENERATION.path).append(PACKAGE_INFO))
        .add(MAIN_SOURCE.append(COLLECTION).template("ProjectCollections.java"), collectionsDestination(baseName, mainDestination))
        .add(MAIN_SOURCE.append(COLLECTION).template(PACKAGE_INFO), mainDestination.append(Destination.COLLECTION.path).append(PACKAGE_INFO))
        .add(TEST_SOURCE.append(COLLECTION).template("ProjectCollectionsTest.java"), collectionsTestDestination(baseName, testDestination))
        .and()
      .build();
    //@formatter:on
  }

  private JHipsterDestination packageInfoDestination(JHipsterDestination mainDestination, Destination destination) {
    return mainDestination.append(destination.path()).append(PACKAGE_INFO);
  }

  private JHipsterDestination collectionsDestination(String className, JHipsterDestination mainDestination) {
    return mainDestination.append(Destination.COLLECTION_DOMAIN.path()).append(className + "Collections.java");
  }

  private JHipsterDestination collectionsTestDestination(String className, JHipsterDestination testDestination) {
    return testDestination.append(Destination.COLLECTION_DOMAIN.path()).append(className + "CollectionsTest.java");
  }
}
