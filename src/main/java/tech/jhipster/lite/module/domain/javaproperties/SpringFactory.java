package tech.jhipster.lite.module.domain.javaproperties;

import tech.jhipster.lite.shared.error.domain.Assert;

public final class SpringFactory {

  private final SpringFactoryType type;
  private final PropertyKey key;
  private final PropertyValue value;

  private SpringFactory(SpringFactoryBuilder builder) {
    Assert.notNull("type", builder.type);
    Assert.notNull("key", builder.key);
    Assert.notNull("value", builder.value);

    type = builder.type;
    key = builder.key;
    value = builder.value;
  }

  public static SpringFactoryKeyBuilder builder(SpringFactoryType type) {
    return new SpringFactoryBuilder(type);
  }

  public SpringFactoryType type() {
    return type;
  }

  public PropertyKey key() {
    return key;
  }

  public PropertyValue value() {
    return value;
  }

  private static final class SpringFactoryBuilder implements SpringFactoryKeyBuilder, SpringFactoryValueBuilder {

    private final SpringFactoryType type;
    private PropertyKey key;
    private PropertyValue value;

    private SpringFactoryBuilder(SpringFactoryType type) {
      this.type = type;
    }

    @Override
    public SpringFactoryValueBuilder key(PropertyKey key) {
      this.key = key;

      return this;
    }

    @Override
    public SpringFactory value(PropertyValue value) {
      this.value = value;

      return new SpringFactory(this);
    }
  }

  public interface SpringFactoryKeyBuilder {
    SpringFactoryValueBuilder key(PropertyKey key);
  }

  public interface SpringFactoryValueBuilder {
    SpringFactory value(PropertyValue value);
  }
}
