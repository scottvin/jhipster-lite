package tech.jhipster.lite.module.infrastructure.secondary;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.jhipster.lite.TestFileUtils.*;
import static tech.jhipster.lite.module.domain.JHipsterModulesFixture.emptyModuleContext;

import ch.qos.logback.classic.Level;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tech.jhipster.lite.*;
import tech.jhipster.lite.module.domain.file.TemplateRenderer;
import tech.jhipster.lite.module.domain.properties.JHipsterProjectFolder;
import tech.jhipster.lite.module.domain.startupcommand.*;

@UnitTest
@ExtendWith(LogsSpyExtension.class)
class FileSystemStartupCommandsReadmeCommandsHandlerTest {

  private static final FileSystemStartupCommandsReadmeCommandsHandler handler = new FileSystemStartupCommandsReadmeCommandsHandler(
    new FileSystemReplacer(TemplateRenderer.NOOP)
  );

  @Logs
  private LogsSpy logs;

  @Test
  void shouldAddMavenCommandToReadme() {
    JHipsterProjectFolder projectFolder = projectFolderWithReadme();
    JHipsterStartupCommand command = new MavenStartupCommandLine("clean verify sonar:sonar");

    handler.handle(projectFolder, new JHipsterStartupCommands(List.of(command)), emptyModuleContext());

    assertThat(content(projectFolder.filePath("README.md"))).contains("./mvnw clean verify sonar:sonar");
  }

  @Test
  void shouldNotAddMavenCommandToMavenProjectWithoutReadme() {
    JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty");
    JHipsterStartupCommand command = new MavenStartupCommandLine("clean verify sonar:sonar");

    handler.handle(projectFolder, new JHipsterStartupCommands(List.of(command)), emptyModuleContext());

    logs.shouldHave(Level.DEBUG, "Can't apply optional replacement: ");
  }

  @Test
  void shouldAddGradleCommandToReadme() {
    JHipsterProjectFolder projectFolder = projectFolderWithReadme();
    JHipsterStartupCommand command = new GradleStartupCommandLine("clean build sonar --info");

    handler.handle(projectFolder, new JHipsterStartupCommands(List.of(command)), emptyModuleContext());

    assertThat(content(projectFolder.filePath("README.md"))).contains("./gradlew clean build sonar --info");
  }

  @Test
  void shouldAddDockerComposeCommandToReadme() {
    JHipsterProjectFolder projectFolder = projectFolderWithReadme();
    JHipsterStartupCommand command = new DockerComposeStartupCommandLine(new DockerComposeFile("src/main/docker/sonar.yml"));

    handler.handle(projectFolder, new JHipsterStartupCommands(List.of(command)), emptyModuleContext());

    assertThat(content(projectFolder.filePath("README.md"))).contains("docker compose -f src/main/docker/sonar.yml up -d");
  }

  @ParameterizedTest
  @MethodSource("commandOrderScenarios")
  void shouldAddCommandsToProjectReadmeRespectingInsertOrder(List<JHipsterStartupCommand> commands, String expectedReadmeContent) {
    JHipsterProjectFolder projectFolder = projectFolderWithReadme();

    handler.handle(projectFolder, new JHipsterStartupCommands(commands), emptyModuleContext());

    assertThat(content(projectFolder.filePath("README.md"))).contains(expectedReadmeContent);
  }

  private JHipsterProjectFolder projectFolderWithReadme() {
    JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty");
    copy(Paths.get("src/test/resources/projects/README.md"), projectFolder.filePath("README.md"));
    return projectFolder;
  }

  private static Stream<Arguments> commandOrderScenarios() {
    JHipsterStartupCommand mavenCommand = new MavenStartupCommandLine("clean verify sonar:sonar");
    JHipsterStartupCommand dockerComposeCommand = new DockerComposeStartupCommandLine(new DockerComposeFile("src/main/docker/sonar.yml"));

    return Stream.of(
      Arguments.of(
        List.of(mavenCommand, dockerComposeCommand),
        """
        ```bash
        ./mvnw clean verify sonar:sonar
        ```

        ```bash
        docker compose -f src/main/docker/sonar.yml up -d
        ```
        """
      ),
      Arguments.of(
        List.of(dockerComposeCommand, mavenCommand),
        """
        ```bash
        docker compose -f src/main/docker/sonar.yml up -d
        ```

        ```bash
        ./mvnw clean verify sonar:sonar
        ```
        """
      )
    );
  }
}
