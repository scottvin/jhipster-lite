package tech.jhipster.lite.module.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;

import tech.jhipster.lite.module.domain.file.JHipsterSource;
import tech.jhipster.lite.module.domain.replacement.TextNeedleAfterReplacer;
import tech.jhipster.lite.module.domain.replacement.TextNeedleBeforeReplacer;
import tech.jhipster.lite.shared.error.domain.Assert;

final class JHipsterModuleShortcuts {

  private static final JHipsterProjectFilePath README = path("README.md");
  private static final TextNeedleBeforeReplacer JHIPSTER_DOCUMENTATION_NEEDLE = lineBeforeText("\n<!-- jhipster-needle-documentation -->");
  private static final TextNeedleBeforeReplacer JHIPSTER_LOCAL_ENVIRONMENT_NEEDLE = lineBeforeText(
    "\n<!-- jhipster-needle-localEnvironment -->"
  );

  private static final TextNeedleAfterReplacer JHIPSTER_PREREQUISITES = lineAfterText("\n## Prerequisites");

  private static final JHipsterProjectFilePath SPRING_MAIN_LOG_FILE = path("src/main/resources/logback-spring.xml");
  private static final JHipsterProjectFilePath SPRING_TEST_LOG_FILE = path("src/test/resources/logback.xml");
  private static final TextNeedleBeforeReplacer JHIPSTER_LOGGER_NEEDLE = lineBeforeText("<!-- jhipster-needle-logback-add-log -->");

  private final JHipsterModuleBuilder builder;

  JHipsterModuleShortcuts(JHipsterModuleBuilder builder) {
    Assert.notNull("builder", builder);

    this.builder = builder;
  }

  void documentation(DocumentationTitle title, JHipsterSource source) {
    Assert.notNull("title", title);
    Assert.notNull("source", source);

    String target = "documentation/" + title.filename() + source.extension();
    builder.files().add(source, to(target));

    String markdownLink = "- [" + title.get() + "](" + target + ")";
    builder.optionalReplacements().in(README).add(JHIPSTER_DOCUMENTATION_NEEDLE, markdownLink);
  }

  void localEnvironment(LocalEnvironment localEnvironment) {
    Assert.notNull("localEnvironment", localEnvironment);

    builder.optionalReplacements().in(README).add(JHIPSTER_LOCAL_ENVIRONMENT_NEEDLE, localEnvironment.get());
  }

  void prerequisites(String prerequisites) {
    Assert.notBlank("prerequisites", prerequisites);
    builder.optionalReplacements().in(README).add(JHIPSTER_PREREQUISITES, prerequisites);
  }

  void springTestLogger(String name, LogLevel level) {
    Assert.notBlank("name", name);
    Assert.notNull("level", level);

    builder.optionalReplacements().in(SPRING_TEST_LOG_FILE).add(JHIPSTER_LOGGER_NEEDLE, logger(name, level));
  }

  void springMainLogger(String name, LogLevel level) {
    Assert.notBlank("name", name);
    Assert.notNull("level", level);

    builder.optionalReplacements().in(SPRING_MAIN_LOG_FILE).add(JHIPSTER_LOGGER_NEEDLE, logger(name, level));
  }

  private String logger(String name, LogLevel level) {
    return new StringBuilder()
      .append(builder.indentation().spaces())
      .append("<logger name=\"")
      .append(name)
      .append("\" level=\"")
      .append(level.value())
      .append("\" />")
      .toString();
  }

  public void integrationTestExtension(String extensionClass) {
    Assert.notBlank("extensionClass", extensionClass);

    builder
      .mandatoryReplacements()
      .in(path("src/test/java").append(builder.packagePath()).append("IntegrationTest.java"))
      .add(
        lineBeforeText("import org.springframework.boot.test.context.SpringBootTest;"),
        "import org.junit.jupiter.api.extension.ExtendWith;"
      )
      .add(lineBeforeText("public @interface"), "@ExtendWith(" + extensionClass + ".class)");
  }
}
