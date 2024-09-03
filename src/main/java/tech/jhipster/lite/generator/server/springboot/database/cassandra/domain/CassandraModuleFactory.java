package tech.jhipster.lite.generator.server.springboot.database.cassandra.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;

import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.LogLevel;
import tech.jhipster.lite.module.domain.docker.DockerImages;
import tech.jhipster.lite.module.domain.file.JHipsterSource;
import tech.jhipster.lite.module.domain.javadependency.JavaDependency;
import tech.jhipster.lite.module.domain.javadependency.JavaDependencyScope;
import tech.jhipster.lite.module.domain.javaproperties.PropertyKey;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.shared.error.domain.Assert;

public class CassandraModuleFactory {

  private static final JHipsterSource SOURCE = from("server/springboot/database/cassandra");
  private static final String CASSANDRA_SECONDARY = "wire/cassandra/infrastructure/secondary";
  private static final String DC = "datacenter1";
  private static final PropertyKey LOCAL_DATACENTER_PROPERTY = propertyKey("spring.cassandra.local-datacenter");
  private final DockerImages dockerImages;

  public CassandraModuleFactory(DockerImages dockerImages) {
    this.dockerImages = dockerImages;
  }

  public JHipsterModule buildModule(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    String packagePath = properties.packagePath();
    String packageName = properties.basePackage().get() + ".";

    //@formatter:off
    return moduleBuilder(properties)
      .javaDependencies()
        .addDependency(groupId("org.springframework.boot"), artifactId("spring-boot-starter-data-cassandra"))
        .addDependency(testContainerDependency())
        .and()
      .documentation(documentationTitle("Cassandra"), SOURCE.file("cassandra.md"))
      .startupCommands()
        .dockerCompose("src/main/docker/cassandra.yml")
        .and()
      .context()
        .put("cassandraDockerImage", dockerImages.get("cassandra").fullName())
        .put("DC", DC)
        .and()
      .files()
        .batch(SOURCE, toSrcMainJava().append(packagePath).append(CASSANDRA_SECONDARY))
          .addTemplate("CassandraDatabaseConfiguration.java")
          .addTemplate("CassandraJSR310DateConverters.java")
          .and()
        .add(
          SOURCE.template("CassandraJSR310DateConvertersTest.java"),
          toSrcTestJava().append(packagePath).append(CASSANDRA_SECONDARY).append("CassandraJSR310DateConvertersTest.java")
        )
        .add(SOURCE.template("cassandra.yml"), toSrcMainDocker().append("cassandra.yml"))
        .add(SOURCE.template("TestCassandraManager.java"), toSrcTestJava().append(packagePath).append("TestCassandraManager.java"))
        .add(SOURCE.template("CassandraKeyspaceIT.java"), toSrcTestJava().append(packagePath).append("CassandraKeyspaceIT.java"))
        .and()
      .springMainProperties()
        .set(propertyKey("spring.cassandra.contact-points"), propertyValue("127.0.0.1"))
        .comment(LOCAL_DATACENTER_PROPERTY, comment("keyspace-name: yourKeyspace"))
        .set(propertyKey("spring.cassandra.port"), propertyValue(9042))
        .set(LOCAL_DATACENTER_PROPERTY, propertyValue(DC))
        .set(propertyKey("spring.cassandra.schema-action"), propertyValue("none"))
        .and()
      .springTestProperties()
        .set(propertyKey("spring.cassandra.port"), propertyValue("${TEST_CASSANDRA_PORT}"))
        .set(propertyKey("spring.cassandra.contact-points"), propertyValue("${TEST_CASSANDRA_CONTACT_POINT}"))
        .set(LOCAL_DATACENTER_PROPERTY, propertyValue("${TEST_CASSANDRA_DC}"))
        .set(propertyKey("spring.cassandra.keyspace-name"), propertyValue("${TEST_CASSANDRA_KEYSPACE}"))
        .and()
      .springTestFactories()
        .append(propertyKey("org.springframework.context.ApplicationListener"), propertyValue(packageName + "TestCassandraManager"))
        .and()
      .springMainLogger("com.datastax", LogLevel.WARN)
      .springTestLogger("com.datastax", LogLevel.WARN)
      .springTestLogger("org.testcontainers", LogLevel.WARN)
      .build();
    //@formatter:on
  }

  private JavaDependency testContainerDependency() {
    return javaDependency()
      .groupId("org.testcontainers")
      .artifactId("cassandra")
      .versionSlug("testcontainers")
      .scope(JavaDependencyScope.TEST)
      .build();
  }
}
