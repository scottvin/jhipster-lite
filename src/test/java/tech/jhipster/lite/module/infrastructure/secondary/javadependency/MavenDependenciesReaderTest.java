package tech.jhipster.lite.module.infrastructure.secondary.javadependency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.ProjectFiles;
import tech.jhipster.lite.module.domain.javabuild.VersionSlug;

@UnitTest
class MavenDependenciesReaderTest {

  private MavenDependenciesReader reader;

  @BeforeEach
  void loadReader() {
    ProjectFiles files = mock(ProjectFiles.class);
    when(files.readString(anyString())).thenAnswer(fileContent());

    reader = new MavenDependenciesReader(files);
  }

  private Answer<String> fileContent() {
    return invocation -> Files.readString(Paths.get("src/main/resources/" + invocation.getArgument(0, String.class)));
  }

  @Test
  void shouldReadJavaDependencies() {
    assertThat(reader.get().get(new VersionSlug("json-web-token"))).isNotNull();
  }
}
