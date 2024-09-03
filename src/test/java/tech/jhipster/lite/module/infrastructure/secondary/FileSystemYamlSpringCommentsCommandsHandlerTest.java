package tech.jhipster.lite.module.infrastructure.secondary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static tech.jhipster.lite.TestFileUtils.content;
import static tech.jhipster.lite.TestFileUtils.loadDefaultProperties;
import static tech.jhipster.lite.module.domain.JHipsterModule.comment;
import static tech.jhipster.lite.module.domain.JHipsterModule.propertyKey;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.Indentation;
import tech.jhipster.lite.module.domain.javaproperties.SpringComment;
import tech.jhipster.lite.module.domain.javaproperties.SpringComments;
import tech.jhipster.lite.module.domain.javaproperties.SpringPropertyType;
import tech.jhipster.lite.module.domain.properties.JHipsterProjectFolder;

@UnitTest
class FileSystemYamlSpringCommentsCommandsHandlerTest {

  public static final Path EXISTING_SPRING_CONFIGURATION = Paths.get(
    "src/test/resources/projects/project-with-spring-application-yaml/application.yml"
  );
  private static final FileSystemYamlSpringCommentsCommandsHandler handler = new FileSystemYamlSpringCommentsCommandsHandler();

  @ParameterizedTest
  @ValueSource(strings = { "src/main/resources/config/application.yml", "src/main/resources/application.yml" })
  void shouldCommentMainProperties(String propertiesPath) {
    String path = TestFileUtils.tmpDirForTest();
    Path propertiesFile = Paths.get(path, propertiesPath);
    loadDefaultProperties(EXISTING_SPRING_CONFIGURATION, propertiesFile);

    handler.handle(Indentation.DEFAULT, folder(path), commentOn(SpringPropertyType.MAIN_PROPERTIES, "spring.application.name"));

    assertThat(content(propertiesFile)).contains(
      """
          # This is a comment
          name: JHLite # This is the name of the application\
      """
    );
  }

  @ParameterizedTest
  @ValueSource(strings = { "src/main/resources/config/bootstrap.yml", "src/main/resources/bootstrap.yml" })
  void shouldCommentMainBootStrapProperties(String propertiesPath) {
    String path = TestFileUtils.tmpDirForTest();
    Path propertiesFile = Paths.get(path, propertiesPath);
    loadDefaultProperties(EXISTING_SPRING_CONFIGURATION, propertiesFile);

    handler.handle(Indentation.DEFAULT, folder(path), commentOn(SpringPropertyType.MAIN_BOOTSTRAP_PROPERTIES, "spring.application.name"));

    assertThat(content(propertiesFile)).contains(
      """
          # This is a comment
          name: JHLite # This is the name of the application\
      """
    );
  }

  @ParameterizedTest
  @ValueSource(strings = { "src/test/resources/config/application.yml", "src/test/resources/application.yml" })
  void shouldCommentTestProperties(String propertiesPath) {
    String path = TestFileUtils.tmpDirForTest();
    Path propertiesFile = Paths.get(path, propertiesPath);
    loadDefaultProperties(EXISTING_SPRING_CONFIGURATION, propertiesFile);

    handler.handle(Indentation.DEFAULT, folder(path), commentOn(SpringPropertyType.TEST_PROPERTIES, "spring.application.name"));

    assertThat(content(propertiesFile)).contains(
      """
          # This is a comment
          name: JHLite # This is the name of the application\
      """
    );
  }

  @ParameterizedTest
  @ValueSource(strings = { "src/test/resources/config/bootstrap.yml", "src/test/resources/bootstrap.yml" })
  void shouldCommentTestBootStrapProperties(String propertiesPath) {
    String path = TestFileUtils.tmpDirForTest();
    Path propertiesFile = Paths.get(path, propertiesPath);
    loadDefaultProperties(EXISTING_SPRING_CONFIGURATION, propertiesFile);

    handler.handle(Indentation.DEFAULT, folder(path), commentOn(SpringPropertyType.TEST_BOOTSTRAP_PROPERTIES, "spring.application.name"));

    assertThat(content(propertiesFile)).contains(
      """
          # This is a comment
          name: JHLite # This is the name of the application\
      """
    );
  }

  @Test
  void shouldNotCommentWhenFileNotExists() {
    String path = TestFileUtils.tmpDirForTest();

    handler.handle(Indentation.DEFAULT, folder(path), commentOn(SpringPropertyType.MAIN_PROPERTIES, "spring.application.name"));

    Throwable thrown = catchThrowable(() -> content(Paths.get(path, "src/main/resources/config/application.yml")));
    assertThat(thrown).hasCauseInstanceOf(NoSuchFileException.class);
  }

  @Test
  void shouldNotCommentWhenPropertyKeyDoesNotExist() {
    String path = TestFileUtils.tmpDirForTest();
    Path propertiesFile = Paths.get(path, "src/main/resources/config/application.yml");
    loadDefaultProperties(EXISTING_SPRING_CONFIGURATION, propertiesFile);

    handler.handle(
      Indentation.DEFAULT,
      folder(path),
      commentOn(SpringPropertyType.MAIN_PROPERTIES, "springdoc.swagger-ui.operationsSorter")
    );

    assertThat(content(propertiesFile)).doesNotContain(
      """
      # This is a comment
      springdoc.swagger-ui.operationsSorter"""
    );
  }

  @ParameterizedTest
  @ValueSource(strings = { "spring.application.name", "logging.level.tech.jhipster.lite" })
  void shouldOverwritePreviousComment(String propertyKey) {
    String path = TestFileUtils.tmpDirForTest();
    Path propertiesFile = Paths.get(path, "src/main/resources/config/application.yml");
    loadDefaultProperties(EXISTING_SPRING_CONFIGURATION, propertiesFile);

    handler.handle(Indentation.DEFAULT, folder(path), commentOn(SpringPropertyType.MAIN_PROPERTIES, "spring.application.name"));
    handler.handle(Indentation.DEFAULT, folder(path), commentOn(SpringPropertyType.MAIN_PROPERTIES, "spring.application.name"));

    assertThat(content(propertiesFile)).doesNotContain(
      """
      spring:
        application:
          # This is a comment
          # This is a comment
          name: JHLite # This is the name of the application
      """
    );
    assertThat(content(propertiesFile)).contains(
      """
      spring:
        application:
          # This is a comment
          name: JHLite # This is the name of the application
      """
    );
  }

  @Test
  void shouldNotOrganizePropertiesWhenFileNotExists() {
    String path = TestFileUtils.tmpDirForTest();

    handler.handle(Indentation.DEFAULT, folder(path), emptySpringComments());

    Throwable thrown = catchThrowable(() -> content(Paths.get(path, "src/main/resources/config/application.yml")));
    assertThat(thrown).hasCauseInstanceOf(NoSuchFileException.class);
  }

  private JHipsterProjectFolder folder(String path) {
    return new JHipsterProjectFolder(path);
  }

  private SpringComments commentOn(SpringPropertyType springPropertyType, String propertyKey) {
    return new SpringComments(
      List.of(SpringComment.builder(springPropertyType).key(propertyKey(propertyKey)).comment(comment("This is a comment")).build())
    );
  }

  private SpringComments emptySpringComments() {
    return new SpringComments(List.of());
  }
}
