package tech.jhipster.lite.generator.server.springboot.dbmigration.neo4jmigrations.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;

import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.file.JHipsterSource;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.shared.error.domain.Assert;

public class Neo4jMigrationsModuleFactory {

  private static final JHipsterSource SOURCE = from("server/springboot/database/neo4j-migrations");

  public JHipsterModule buildModule(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    //@formatter:off
    return moduleBuilder(properties)
      .javaDependencies()
        .addDependency(groupId("eu.michael-simons.neo4j"), artifactId("neo4j-migrations-spring-boot-starter"), versionSlug("neo4j-migrations"))
        .and()
      .documentation(documentationTitle("Neo4j Migrations"), SOURCE.append("neo4j-migrations.md"))
      .springMainProperties()
        .set(propertyKey("org.neo4j.migrations.check-location"), propertyValue(false))
        .and()
      .build();
    //@formatter:on
  }
}
