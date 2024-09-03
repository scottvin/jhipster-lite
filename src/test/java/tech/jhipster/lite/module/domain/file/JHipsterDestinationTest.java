package tech.jhipster.lite.module.domain.file;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.FileSystems;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.properties.JHipsterProjectFolder;

@UnitTest
class JHipsterDestinationTest {

  private static final String SEPARATOR = FileSystems.getDefault().getSeparator();

  private static final JHipsterProjectFolder PROJECT = new JHipsterProjectFolder(TestFileUtils.tmpDirForTest());

  @Test
  void shouldAddSlashWhenHappeningElementWithoutSlash() {
    assertThat(new JHipsterDestination("src/main").append("file").pathInProject(PROJECT).toString()).endsWith(path("src", "main", "file"));
  }

  @Test
  void shouldDeduplicateSlashes() {
    assertThat(new JHipsterDestination("src/main/").append("/file").pathInProject(PROJECT).toString()).endsWith(
      path("src", "main", "file")
    );
  }

  @Test
  void testToStringShowsDestination() {
    assertThat(new JHipsterDestination("src")).hasToString("src");
  }

  private static String path(String... part) {
    return String.join(SEPARATOR, part);
  }
}
