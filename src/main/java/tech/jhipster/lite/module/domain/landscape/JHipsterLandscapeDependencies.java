package tech.jhipster.lite.module.domain.landscape;

import static java.util.function.Predicate.*;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import tech.jhipster.lite.shared.error.domain.Assert;

public record JHipsterLandscapeDependencies(Collection<? extends JHipsterLandscapeDependency> dependencies) {
  public JHipsterLandscapeDependencies {
    Assert.notEmpty("dependencies", dependencies);
  }

  public static Optional<JHipsterLandscapeDependencies> of(Collection<? extends JHipsterLandscapeDependency> dependencies) {
    return Optional.ofNullable(dependencies).filter(not(Collection::isEmpty)).map(JHipsterLandscapeDependencies::new);
  }

  public long count() {
    return dependencies().size();
  }

  public Stream<JHipsterLandscapeDependency> stream() {
    return dependencies().stream().map(JHipsterLandscapeDependency.class::cast);
  }
}
