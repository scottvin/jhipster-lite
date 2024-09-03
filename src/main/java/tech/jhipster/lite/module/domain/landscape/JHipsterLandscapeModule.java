package tech.jhipster.lite.module.domain.landscape;

import static org.apache.commons.lang3.builder.ToStringStyle.*;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import tech.jhipster.lite.module.domain.JHipsterModuleSlug;
import tech.jhipster.lite.module.domain.JHipsterSlug;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleOperation;
import tech.jhipster.lite.module.domain.resource.JHipsterModulePropertiesDefinition;
import tech.jhipster.lite.shared.error.domain.Assert;
import tech.jhipster.lite.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;

public final class JHipsterLandscapeModule implements JHipsterLandscapeElement {

  private final JHipsterModuleSlug module;
  private final JHipsterModuleOperation operation;
  private final JHipsterModulePropertiesDefinition propertiesDefinition;
  private final Optional<JHipsterLandscapeDependencies> dependencies;

  private JHipsterLandscapeModule(JHipsterLandscapeModuleBuilder builder) {
    Assert.notNull("module", builder.module);
    Assert.notNull("operation", builder.operation);
    Assert.notNull("propertiesDefinition", builder.propertiesDefinition);

    module = builder.module;
    operation = builder.operation;
    propertiesDefinition = builder.propertiesDefinition;
    dependencies = JHipsterLandscapeDependencies.of(builder.dependencies);
  }

  public static JHipsterLandscapeModuleSlugBuilder builder() {
    return new JHipsterLandscapeModuleBuilder();
  }

  @Override
  public JHipsterModuleSlug slug() {
    return module;
  }

  public JHipsterModuleOperation operation() {
    return operation;
  }

  public JHipsterModulePropertiesDefinition propertiesDefinition() {
    return propertiesDefinition;
  }

  @Override
  public Optional<JHipsterLandscapeDependencies> dependencies() {
    return dependencies;
  }

  @Override
  public Stream<JHipsterLandscapeModule> allModules() {
    return Stream.of(this);
  }

  @Override
  public Stream<JHipsterSlug> slugs() {
    return Stream.of(slug());
  }

  @Override
  @ExcludeFromGeneratedCodeCoverage
  public int hashCode() {
    return new HashCodeBuilder().append(module).hashCode();
  }

  @Override
  @ExcludeFromGeneratedCodeCoverage
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    JHipsterLandscapeModule other = (JHipsterLandscapeModule) obj;

    return new EqualsBuilder().append(module, other.module).isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
      .append("module", module)
      .append("operation", operation)
      .append("propertiesDefinition", propertiesDefinition)
      .append("dependencies", dependencies)
      .build();
  }

  private static final class JHipsterLandscapeModuleBuilder
    implements
      JHipsterLandscapeModuleSlugBuilder,
      JHipsterLandscapeModuleOperationBuilder,
      JHipsterLandscapeModulePropertiesDefinitionBuilder,
      JHipsterLandscapeModuleDependenciesBuilder {

    private JHipsterModuleSlug module;
    private JHipsterModuleOperation operation;
    private Collection<? extends JHipsterLandscapeDependency> dependencies;
    private JHipsterModulePropertiesDefinition propertiesDefinition;

    @Override
    public JHipsterLandscapeModuleOperationBuilder module(JHipsterModuleSlug module) {
      this.module = module;

      return this;
    }

    @Override
    public JHipsterLandscapeModulePropertiesDefinitionBuilder operation(JHipsterModuleOperation operation) {
      this.operation = operation;

      return this;
    }

    @Override
    public JHipsterLandscapeModuleDependenciesBuilder propertiesDefinition(JHipsterModulePropertiesDefinition propertiesDefinition) {
      this.propertiesDefinition = propertiesDefinition;

      return this;
    }

    @Override
    public JHipsterLandscapeModule dependencies(Collection<? extends JHipsterLandscapeDependency> dependencies) {
      this.dependencies = dependencies;

      return new JHipsterLandscapeModule(this);
    }
  }

  public interface JHipsterLandscapeModuleSlugBuilder {
    JHipsterLandscapeModuleOperationBuilder module(JHipsterModuleSlug module);

    default JHipsterLandscapeModuleOperationBuilder module(String module) {
      return module(new JHipsterModuleSlug(module));
    }
  }

  public interface JHipsterLandscapeModuleOperationBuilder {
    JHipsterLandscapeModulePropertiesDefinitionBuilder operation(JHipsterModuleOperation operation);

    default JHipsterLandscapeModulePropertiesDefinitionBuilder operation(String operation) {
      return operation(new JHipsterModuleOperation(operation));
    }
  }

  public interface JHipsterLandscapeModulePropertiesDefinitionBuilder {
    JHipsterLandscapeModuleDependenciesBuilder propertiesDefinition(JHipsterModulePropertiesDefinition propertiesDefinition);
  }

  public interface JHipsterLandscapeModuleDependenciesBuilder {
    JHipsterLandscapeModule dependencies(Collection<? extends JHipsterLandscapeDependency> dependencies);

    default JHipsterLandscapeModule withoutDependencies() {
      return dependencies(null);
    }
  }
}
