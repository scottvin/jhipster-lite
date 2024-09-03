package tech.jhipster.lite.module.domain.landscape;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import tech.jhipster.lite.module.domain.JHipsterFeatureSlug;
import tech.jhipster.lite.module.domain.JHipsterSlug;
import tech.jhipster.lite.shared.error.domain.Assert;

public final class JHipsterLandscapeFeature implements JHipsterLandscapeElement {

  private final JHipsterFeatureSlug slug;
  private final Collection<JHipsterLandscapeModule> modules;
  private final Optional<JHipsterLandscapeDependencies> dependencies;

  public JHipsterLandscapeFeature(JHipsterFeatureSlug slug, Collection<JHipsterLandscapeModule> modules) {
    Assert.notNull("slug", slug);
    Assert.notNull("modules", modules);

    this.slug = slug;
    this.modules = modules;
    dependencies = buildDependencies();
  }

  private Optional<JHipsterLandscapeDependencies> buildDependencies() {
    return JHipsterLandscapeDependencies.of(
      modules()
        .stream()
        .map(JHipsterLandscapeModule::dependencies)
        .flatMap(Optional::stream)
        .flatMap(JHipsterLandscapeDependencies::stream)
        .toList()
    );
  }

  @Override
  public JHipsterFeatureSlug slug() {
    return slug;
  }

  public Collection<JHipsterLandscapeModule> modules() {
    return modules;
  }

  @Override
  public Optional<JHipsterLandscapeDependencies> dependencies() {
    return dependencies;
  }

  @Override
  public Stream<JHipsterLandscapeModule> allModules() {
    return modules.stream();
  }

  @Override
  public Stream<JHipsterSlug> slugs() {
    return Stream.concat(Stream.of(slug()), allModules().map(JHipsterLandscapeModule::slug));
  }
}
