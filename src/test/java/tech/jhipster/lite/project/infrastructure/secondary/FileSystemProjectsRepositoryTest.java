package tech.jhipster.lite.project.infrastructure.secondary;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static tech.jhipster.lite.project.domain.history.ProjectHistoryFixture.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.JsonHelper;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.ProjectFiles;
import tech.jhipster.lite.project.domain.ModuleSlug;
import tech.jhipster.lite.project.domain.ModulesSlugs;
import tech.jhipster.lite.project.domain.ProjectPath;
import tech.jhipster.lite.project.domain.download.Project;
import tech.jhipster.lite.project.domain.download.ProjectName;
import tech.jhipster.lite.project.domain.history.ProjectHistory;
import tech.jhipster.lite.project.domain.preset.Preset;
import tech.jhipster.lite.project.domain.preset.PresetName;
import tech.jhipster.lite.shared.error.domain.GeneratorException;

@UnitTest
class FileSystemProjectsRepositoryTest {

  private static final String DEFAULT_PRESET_FILE = "preset.json";
  private static final FileSystemProjectsRepository projects = new FileSystemProjectsRepository(
    JsonHelper.jsonMapper(),
    mock(ProjectFormatter.class),
    mock(ProjectFiles.class),
    DEFAULT_PRESET_FILE
  );

  @Nested
  @DisplayName("Download")
  class FileSystemProjectsRepositoryDownloadTest {

    @Test
    void shouldGetEmptyProjectFromUnknownFolder() {
      assertThat(projects.get(new ProjectPath(TestFileUtils.tmpDirForTest()))).isEmpty();
    }

    @Test
    void shouldGetEmptyProjectFromEmptyFolder() throws IOException {
      String folder = TestFileUtils.tmpDirForTest();
      Files.createDirectories(Paths.get(folder));

      assertThat(projects.get(new ProjectPath(folder))).isEmpty();
    }

    @Test
    void shouldGetEmptyProjectFromFile() {
      ProjectPath path = folder().add("src/test/resources/projects/maven/pom.xml").build();

      ProjectPath filePath = new ProjectPath(path.get() + "/pom.xml");

      assertThat(projects.get(filePath)).isEmpty();
    }

    @Test
    void shouldGetZippedProjectFromFolderWithoutPackageJson() {
      ProjectPath path = folder().add("src/test/resources/projects/maven/pom.xml").build();

      Project project = projects.get(path).get();

      assertThat(project.name()).isEqualTo(ProjectName.DEFAULT);
      assertThat(zippedFiles(project.content())).containsExactly("pom.xml");
    }

    @Test
    void shouldGetZippedProjectFromFolderEmptyPackageJson() {
      ProjectPath path = folder().add("src/test/resources/projects/empty-package-json/package.json").build();

      Project project = projects.get(path).get();

      assertThat(project.name()).isEqualTo(ProjectName.DEFAULT);
      assertThat(zippedFiles(project.content())).containsExactly("package.json");
    }

    @Test
    void shouldGetZippedProjectFromFolderPackageJsonWithProjectName() {
      ProjectPath path = folder().add("src/test/resources/projects/package-json/package.json").build();

      Project project = projects.get(path).get();

      assertThat(project.name()).isEqualTo(new ProjectName("jhipster-project"));
      assertThat(zippedFiles(project.content())).containsExactly("package.json");
    }

    @Test
    void shouldNotGetNodeModulesInZipFile() {
      ProjectPath path = folder()
        .add("src/test/resources/projects/node/package.json", "beer.json")
        .add("src/test/resources/projects/node/package.json", "dummy/beer.json")
        .add("src/test/resources/projects/node/package.json", "node_modules/package.json")
        .build();

      Project project = projects.get(path).get();

      assertThat(zippedFiles(project.content()))
        .doesNotContain("node_modules", "node_modules/package.json")
        .contains("beer.json", "dummy" + FileSystems.getDefault().getSeparator() + "beer.json");
    }

    private Collection<String> zippedFiles(byte[] content) {
      try (ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(content))) {
        ZipEntry entry = zip.getNextEntry();

        List<String> result = new ArrayList<>();
        while (entry != null) {
          result.add(entry.getName());

          entry = zip.getNextEntry();
        }

        return result;
      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }
  }

  @Nested
  @DisplayName("Save history")
  class FileSystemProjectsRepositorySaveHistoryTest {

    @Test
    void shouldHandleSerializationError() throws JsonProcessingException {
      ObjectWriter writer = mock(ObjectWriter.class);
      when(writer.writeValueAsBytes(any())).thenThrow(JsonProcessingException.class);

      ObjectMapper json = mock(ObjectMapper.class);
      when(json.writerWithDefaultPrettyPrinter()).thenReturn(writer);

      FileSystemProjectsRepository fileSystemProjectsRepository = new FileSystemProjectsRepository(
        json,
        mock(ProjectFormatter.class),
        mock(ProjectFiles.class),
        DEFAULT_PRESET_FILE
      );

      assertThatThrownBy(() -> fileSystemProjectsRepository.save(projectHistory())).isExactlyInstanceOf(GeneratorException.class);
    }

    @Test
    void shouldSaveHistory() throws IOException {
      ProjectPath path = folder().build();

      projects.save(new ProjectHistory(path, List.of(projectAction())));

      assertThat(Files.readString(Paths.get(path.get(), ".jhipster/modules", "history.json"))).isEqualToIgnoringWhitespace(
        """
        {
          "actions" : [
            {
              "module" : "test-module",
              "date" : "2021-12-03T10:15:30Z",
              "properties" : {
                "key" : "value"
              }
            }
          ]
        }
        """
      );
    }
  }

  @Nested
  @DisplayName("Get history")
  class FileSystemProjectsRepositoryGetHistoryTest {

    @Test
    void shouldHandleDeserializationErrors() throws IOException {
      ProjectPath path = folder().add("src/test/resources/projects/history/history.json", ".jhipster/modules/history.json").build();
      ObjectMapper json = mock(ObjectMapper.class);
      when(json.readValue(any(byte[].class), eq(PersistedProjectHistory.class))).thenThrow(IOException.class);

      FileSystemProjectsRepository fileSystemProjectsRepository = new FileSystemProjectsRepository(
        json,
        mock(ProjectFormatter.class),
        mock(ProjectFiles.class),
        DEFAULT_PRESET_FILE
      );

      assertThatThrownBy(() -> fileSystemProjectsRepository.getHistory(path)).isExactlyInstanceOf(GeneratorException.class);
    }

    @Test
    void shouldGetEmptyHistoryFromUnknownFile() {
      ProjectPath path = folder().build();

      ProjectHistory history = projects.getHistory(path);

      assertThat(history.path()).isEqualTo(path);
      assertThat(history.actions()).isEmpty();
    }

    @Test
    void shouldGetExistingHistory() {
      ProjectPath path = folder().add("src/test/resources/projects/history/history.json", ".jhipster/modules/history.json").build();

      ProjectHistory history = projects.getHistory(path);

      assertThat(history.path()).isEqualTo(path);
      assertThat(history.actions()).usingRecursiveFieldByFieldElementComparator().containsExactly(projectAction());
    }
  }

  @Nested
  @DisplayName("Get preset")
  class FileSystemProjectsRepositoryGetPresetTest {

    @Test
    void shouldHandleDeserializationErrors() throws IOException {
      ObjectMapper json = mock(ObjectMapper.class);
      when(json.readValue(any(byte[].class), eq(PersistedPresets.class))).thenThrow(IOException.class);
      FileSystemProjectsRepository fileSystemProjectsRepository = new FileSystemProjectsRepository(
        json,
        mock(ProjectFormatter.class),
        mockProjectFilesWithValidPresetJson(),
        DEFAULT_PRESET_FILE
      );

      assertThatThrownBy(fileSystemProjectsRepository::getPresets).isExactlyInstanceOf(GeneratorException.class);
    }

    @Test
    void shouldNotReturnPresetFromUnknownFile() {
      ProjectFiles projectFiles = mock(ProjectFiles.class);
      lenient().when(projectFiles.readBytes("/preset.json")).thenThrow(GeneratorException.class);
      FileSystemProjectsRepository fileSystemProjectsRepository = new FileSystemProjectsRepository(
        JsonHelper.jsonMapper(),
        mock(ProjectFormatter.class),
        projectFiles,
        DEFAULT_PRESET_FILE
      );

      assertThatThrownBy(fileSystemProjectsRepository::getPresets).isExactlyInstanceOf(GeneratorException.class);
    }

    @Test
    void shouldGetExistingPreset() {
      FileSystemProjectsRepository fileSystemProjectsRepository = new FileSystemProjectsRepository(
        JsonHelper.jsonMapper(),
        mock(ProjectFormatter.class),
        mockProjectFilesWithValidPresetJson(),
        DEFAULT_PRESET_FILE
      );

      Collection<Preset> presets = fileSystemProjectsRepository.getPresets();

      assertThat(presets).containsExactly(expectedPreset());
    }
  }

  private static FolderBuilder folder() {
    return new FolderBuilder();
  }

  private static class FolderBuilder {

    private final Path folder;

    public FolderBuilder() {
      folder = Paths.get(TestFileUtils.tmpDirForTest());

      createFolder();
    }

    private void createFolder() throws AssertionError {
      try {
        Files.createDirectories(folder);
      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }

    public FolderBuilder add(String source) {
      return add(source, Paths.get(source).getFileName().toString());
    }

    public FolderBuilder add(String source, String destination) {
      Path sourcePath = Paths.get(source);

      try {
        Path destinationPath = folder.resolve(destination);
        Files.createDirectories(destinationPath.getParent());
        Files.copy(sourcePath, destinationPath);
      } catch (IOException e) {
        throw new AssertionError(e);
      }

      return this;
    }

    public ProjectPath build() {
      return new ProjectPath(folder.toString());
    }
  }

  private static ProjectFiles mockProjectFilesWithValidPresetJson() {
    ProjectFiles projectFiles = mock(ProjectFiles.class);

    String validPresetJson =
      """
      {
        "presets": [
          {
            "name": "angular + spring boot",
            "modules": [
              "init",
              "application-service-hexagonal-architecture-documentation",
              "maven-java",
              "prettier",
              "angular-core",
              "java-base",
              "maven-wrapper",
              "spring-boot",
              "spring-boot-mvc-empty",
              "logs-spy",
              "spring-boot-tomcat"
            ]
          }
        ]
      }
      """;
    lenient().when(projectFiles.readBytes("/preset.json")).thenReturn(validPresetJson.getBytes());

    return projectFiles;
  }

  private static Preset expectedPreset() {
    return new Preset(
      new PresetName("angular + spring boot"),
      new ModulesSlugs(
        List.of(
          new ModuleSlug("init"),
          new ModuleSlug("application-service-hexagonal-architecture-documentation"),
          new ModuleSlug("maven-java"),
          new ModuleSlug("prettier"),
          new ModuleSlug("angular-core"),
          new ModuleSlug("java-base"),
          new ModuleSlug("maven-wrapper"),
          new ModuleSlug("spring-boot"),
          new ModuleSlug("spring-boot-mvc-empty"),
          new ModuleSlug("logs-spy"),
          new ModuleSlug("spring-boot-tomcat")
        )
      )
    );
  }
}
