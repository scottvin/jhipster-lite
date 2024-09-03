package tech.jhipster.lite.module.infrastructure.secondary.javabuild;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static tech.jhipster.lite.module.domain.JHipsterModule.*;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.file.JHipsterModuleFiles;
import tech.jhipster.lite.module.domain.file.JHipsterModuleFiles.JHipsterModuleFilesBuilder;
import tech.jhipster.lite.module.domain.javabuild.JavaBuildTool;

@UnitTest
class FileSystemProjectJavaBuildToolRepositoryTest {

  private final FileSystemProjectJavaBuildToolRepository javaBuildTools = new FileSystemProjectJavaBuildToolRepository();

  @Nested
  class DetectFromJHipsterModuleFiles {

    private final JHipsterModuleFilesBuilder moduleFiles = JHipsterModuleFiles.builder(mock(JHipsterModuleBuilder.class));

    @Test
    void shouldReturnMavenWhenPomXmlIsAdded() {
      moduleFiles.add(from("pom.xml"), to("pom.xml"));

      Optional<JavaBuildTool> javaBuildTool = javaBuildTools.detect(moduleFiles.build());

      assertThat(javaBuildTool).contains(JavaBuildTool.MAVEN);
    }

    @Test
    void shouldReturnMavenWhenPomXmlIsMoved() {
      moduleFiles.move(path("pom.xml.old"), to("pom.xml"));

      Optional<JavaBuildTool> javaBuildTool = javaBuildTools.detect(moduleFiles.build());

      assertThat(javaBuildTool).contains(JavaBuildTool.MAVEN);
    }

    @Test
    void shouldReturnGradleWhenBuildGradleFileIsAdded() {
      moduleFiles.add(from("build.gradle.kts"), to("build.gradle.kts"));

      Optional<JavaBuildTool> javaBuildTool = javaBuildTools.detect(moduleFiles.build());

      assertThat(javaBuildTool).contains(JavaBuildTool.GRADLE);
    }

    @Test
    void shouldReturnGradleWhenBuildGradleFileIsMoved() {
      moduleFiles.move(path("build.gradle"), to("build.gradle.kts"));

      Optional<JavaBuildTool> javaBuildTool = javaBuildTools.detect(moduleFiles.build());

      assertThat(javaBuildTool).contains(JavaBuildTool.GRADLE);
    }

    @Test
    void shouldReturnEmptyWhenNoJavaBuildToolIsAdded() {
      Optional<JavaBuildTool> javaBuildTool = javaBuildTools.detect(moduleFiles.build());

      assertThat(javaBuildTool).isEmpty();
    }
  }
}
