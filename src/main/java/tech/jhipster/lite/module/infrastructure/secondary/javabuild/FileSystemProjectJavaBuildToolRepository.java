package tech.jhipster.lite.module.infrastructure.secondary.javabuild;

import java.util.Optional;
import org.springframework.stereotype.Component;
import tech.jhipster.lite.module.domain.file.JHipsterDestination;
import tech.jhipster.lite.module.domain.file.JHipsterFileToMove;
import tech.jhipster.lite.module.domain.file.JHipsterModuleFile;
import tech.jhipster.lite.module.domain.file.JHipsterModuleFiles;
import tech.jhipster.lite.module.domain.javabuild.JavaBuildTool;
import tech.jhipster.lite.module.domain.javabuild.ProjectJavaBuildToolRepository;
import tech.jhipster.lite.module.domain.properties.JHipsterProjectFolder;

@Component
public class FileSystemProjectJavaBuildToolRepository implements ProjectJavaBuildToolRepository {

  private static final String POM_XML = "pom.xml";
  private static final String BUILD_GRADLE_KTS = "build.gradle.kts";

  @Override
  public Optional<JavaBuildTool> detect(JHipsterProjectFolder projectFolder) {
    if (projectFolder.fileExists(POM_XML)) {
      return Optional.of(JavaBuildTool.MAVEN);
    }

    if (projectFolder.fileExists(BUILD_GRADLE_KTS)) {
      return Optional.of(JavaBuildTool.GRADLE);
    }

    return Optional.empty();
  }

  @Override
  public Optional<JavaBuildTool> detect(JHipsterModuleFiles moduleFiles) {
    if (hasMavenFile(moduleFiles)) {
      return Optional.of(JavaBuildTool.MAVEN);
    }

    if (hasGradleFile(moduleFiles)) {
      return Optional.of(JavaBuildTool.GRADLE);
    }

    return Optional.empty();
  }

  private static boolean hasMavenFile(JHipsterModuleFiles moduleFiles) {
    return hasBuildFiles(moduleFiles, POM_XML);
  }

  private static boolean hasGradleFile(JHipsterModuleFiles moduleFiles) {
    return hasBuildFiles(moduleFiles, BUILD_GRADLE_KTS);
  }

  private static boolean hasBuildFiles(JHipsterModuleFiles moduleFiles, String file) {
    return hasFileToAdd(moduleFiles, file) || hasFileToMove(moduleFiles, file);
  }

  private static boolean hasFileToMove(JHipsterModuleFiles files, String fileName) {
    return files.filesToMove().stream().map(JHipsterFileToMove::destination).map(JHipsterDestination::get).anyMatch(fileName::equals);
  }

  private static boolean hasFileToAdd(JHipsterModuleFiles files, String fileName) {
    return files.filesToAdd().stream().map(JHipsterModuleFile::destination).map(JHipsterDestination::get).anyMatch(fileName::equals);
  }
}
