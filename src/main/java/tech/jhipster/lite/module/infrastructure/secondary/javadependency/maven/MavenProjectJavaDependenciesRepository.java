package tech.jhipster.lite.module.infrastructure.secondary.javadependency.maven;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.stereotype.Repository;
import tech.jhipster.lite.module.domain.javabuild.VersionSlug;
import tech.jhipster.lite.module.domain.javadependency.JavaDependencies;
import tech.jhipster.lite.module.domain.javadependency.JavaDependency;
import tech.jhipster.lite.module.domain.javadependency.JavaDependencyScope;
import tech.jhipster.lite.module.domain.javadependency.JavaDependencyType;
import tech.jhipster.lite.module.domain.javadependency.JavaDependencyVersion;
import tech.jhipster.lite.module.domain.javadependency.ProjectJavaDependencies;
import tech.jhipster.lite.module.domain.javadependency.ProjectJavaDependenciesVersions;
import tech.jhipster.lite.module.domain.properties.JHipsterProjectFolder;
import tech.jhipster.lite.module.infrastructure.secondary.javadependency.JHipsterProjectFolderJavaDependenciesReader;
import tech.jhipster.lite.shared.enumeration.domain.Enums;
import tech.jhipster.lite.shared.error.domain.GeneratorException;

@Repository
public class MavenProjectJavaDependenciesRepository implements JHipsterProjectFolderJavaDependenciesReader {

  private static final String POM_XML = "pom.xml";

  @Override
  public ProjectJavaDependencies get(JHipsterProjectFolder folder) {
    Path pomPath = folder.filePath(POM_XML);

    if (Files.notExists(pomPath)) {
      return ProjectJavaDependencies.EMPTY;
    }

    try (InputStream input = Files.newInputStream(pomPath)) {
      MavenXpp3Reader reader = new MavenXpp3Reader();
      Model pomContent = reader.read(input);

      return ProjectJavaDependencies.builder()
        .versions(extractVersions(pomContent))
        .dependenciesManagements(extractDependencyManagement(pomContent))
        .dependencies(extractDependencies(pomContent));
    } catch (IOException | XmlPullParserException e) {
      throw GeneratorException.technicalError("Error reading pom file: " + e.getMessage(), e);
    }
  }

  private ProjectJavaDependenciesVersions extractVersions(Model pomContent) {
    List<JavaDependencyVersion> versions = pomContent
      .getProperties()
      .entrySet()
      .stream()
      .filter(versionProperties())
      .map(toJavaDependencyVersion())
      .toList();

    return new ProjectJavaDependenciesVersions(versions);
  }

  private Predicate<Entry<Object, Object>> versionProperties() {
    return entry -> entry.getKey().toString().endsWith(VersionSlug.SUFFIX);
  }

  private Function<Entry<Object, Object>, JavaDependencyVersion> toJavaDependencyVersion() {
    return entry -> new JavaDependencyVersion(entry.getKey().toString(), entry.getValue().toString());
  }

  private JavaDependencies extractDependencyManagement(Model pomContent) {
    DependencyManagement dependencyManagement = pomContent.getDependencyManagement();

    if (dependencyManagement == null) {
      return JavaDependencies.EMPTY;
    }

    List<JavaDependency> mavenDependencies = dependencyManagement.getDependencies().stream().map(toJavaDependency()).toList();

    return new JavaDependencies(mavenDependencies);
  }

  private JavaDependencies extractDependencies(Model pomContent) {
    List<JavaDependency> mavenDependencies = pomContent.getDependencies().stream().map(toJavaDependency()).toList();

    return new JavaDependencies(mavenDependencies);
  }

  private Function<Dependency, JavaDependency> toJavaDependency() {
    return dependency ->
      JavaDependency.builder()
        .groupId(dependency.getGroupId())
        .artifactId(dependency.getArtifactId())
        .versionSlug(dependency.getVersion())
        .classifier(dependency.getClassifier())
        .optional(dependency.isOptional())
        .scope(Enums.map(MavenScope.from(dependency.getScope()), JavaDependencyScope.class))
        .type(MavenType.from(dependency.getType()).map(type -> Enums.map(type, JavaDependencyType.class)).orElse(null))
        .build();
  }
}
