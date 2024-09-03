package tech.jhipster.lite.module.domain.buildproperties;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import tech.jhipster.lite.module.domain.javabuild.command.JavaBuildCommand;
import tech.jhipster.lite.module.domain.javabuild.command.JavaBuildCommands;
import tech.jhipster.lite.module.domain.javabuild.command.SetBuildProperty;
import tech.jhipster.lite.module.domain.javabuildprofile.BuildProfileId;
import tech.jhipster.lite.shared.collection.domain.JHipsterCollections;
import tech.jhipster.lite.shared.error.domain.Assert;

public final class JHipsterModuleBuildProperties {

  private final Map<PropertyKey, PropertyValue> properties;

  private JHipsterModuleBuildProperties(JHipsterModuleBuildPropertiesBuilder<?> builder) {
    properties = JHipsterCollections.immutable(builder.properties);
  }

  public static <T> JHipsterModuleBuildPropertiesBuilder<T> builder(T parent) {
    return new JHipsterModuleBuildPropertiesBuilder<>(parent);
  }

  public JavaBuildCommands buildChanges() {
    return new JavaBuildCommands(
      properties
        .entrySet()
        .stream()
        .map(property -> new SetBuildProperty(new BuildProperty(property.getKey(), property.getValue())))
        .toList()
    );
  }

  public Stream<JavaBuildCommand> buildChanges(BuildProfileId buildProfile) {
    return properties
      .entrySet()
      .stream()
      .map(property -> new SetBuildProperty(new BuildProperty(property.getKey(), property.getValue()), buildProfile));
  }

  public static final class JHipsterModuleBuildPropertiesBuilder<T> {

    private final T parent;
    private final Map<PropertyKey, PropertyValue> properties = new HashMap<>();

    private JHipsterModuleBuildPropertiesBuilder(T parent) {
      Assert.notNull("parent", parent);

      this.parent = parent;
    }

    public JHipsterModuleBuildPropertiesBuilder<T> set(PropertyKey key, PropertyValue value) {
      Assert.notNull("key", key);
      Assert.notNull("value", value);

      this.properties.put(key, value);

      return this;
    }

    public T and() {
      return parent;
    }

    public JHipsterModuleBuildProperties build() {
      return new JHipsterModuleBuildProperties(this);
    }
  }
}
