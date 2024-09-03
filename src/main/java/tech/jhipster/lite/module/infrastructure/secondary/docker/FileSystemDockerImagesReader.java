package tech.jhipster.lite.module.infrastructure.secondary.docker;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;
import tech.jhipster.lite.module.domain.ProjectFiles;
import tech.jhipster.lite.module.domain.docker.DockerImageVersion;
import tech.jhipster.lite.module.domain.docker.DockerImageVersions;

@Repository
@Order
class FileSystemDockerImagesReader implements DockerImagesReader {

  private static final String DOCKER_FROM = "from ";

  private final ProjectFiles files;

  public FileSystemDockerImagesReader(ProjectFiles files) {
    this.files = files;
  }

  @Override
  public DockerImageVersions get() {
    Collection<DockerImageVersion> versionsRead = Stream.of(files.readString("/generator/dependencies/Dockerfile").split("[\r\n]"))
      .map(String::trim)
      .map(String::toLowerCase)
      .map(toDockerImage())
      .flatMap(Optional::stream)
      .toList();

    return new DockerImageVersions(versionsRead);
  }

  private Function<String, Optional<DockerImageVersion>> toDockerImage() {
    return line -> {
      int versionSeparatorIndex = line.lastIndexOf(':');

      if (versionSeparatorIndex == -1) {
        return Optional.empty();
      }

      return Optional.of(
        new DockerImageVersion(line.substring(DOCKER_FROM.length(), versionSeparatorIndex), line.substring(versionSeparatorIndex + 1))
      );
    };
  }
}
