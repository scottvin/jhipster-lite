package tech.jhipster.lite.generator.server.springboot.dbmigration.flyway.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.file.JHipsterSource;
import tech.jhipster.lite.module.domain.javabuild.GroupId;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.shared.error.domain.Assert;

public class FlywayModuleFactory {

  private static final JHipsterSource SOURCE = from("server/springboot/dbmigration/flyway/resources");

  private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneOffset.UTC);

  private static final GroupId FLYWAY_GROUP_ID = groupId("org.flywaydb");

  private static final String PROPERTIES = "properties";

  public JHipsterModule buildInitializationModule(JHipsterModuleProperties properties) {
    Assert.notNull(PROPERTIES, properties);

    Instant date = properties.getInstantOrDefault("date", Instant.now());

    //@formatter:off
    return moduleBuilder(properties)
      .javaDependencies()
        .addDependency(FLYWAY_GROUP_ID, artifactId("flyway-core"))
        .and()
      .files()
        .add(SOURCE.file("V00000000000000__init.sql"), to("src/main/resources/db/migration").append(initFilename(date)))
        .and()
      .springMainProperties()
        .set(propertyKey("spring.flyway.enabled"), propertyValue(true))
        .set(propertyKey("spring.flyway.locations"), propertyValue("classpath:db/migration"))
        .and()
      .build();
    //@formatter:on
  }

  private String initFilename(Instant date) {
    return "V%s__init.sql".formatted(FILE_DATE_FORMAT.format(date));
  }

  public JHipsterModule buildMysqlDependencyModule(JHipsterModuleProperties properties) {
    Assert.notNull(PROPERTIES, properties);

    return moduleBuilder(properties).javaDependencies().addDependency(FLYWAY_GROUP_ID, artifactId("flyway-mysql")).and().build();
  }

  public JHipsterModule buildPostgresqlDependencyModule(JHipsterModuleProperties properties) {
    Assert.notNull(PROPERTIES, properties);

    return moduleBuilder(properties)
      .javaDependencies()
      .addDependency(FLYWAY_GROUP_ID, artifactId("flyway-database-postgresql"))
      .and()
      .build();
  }

  public JHipsterModule buildMsSqlServerDependencyModule(JHipsterModuleProperties properties) {
    Assert.notNull(PROPERTIES, properties);

    return moduleBuilder(properties).javaDependencies().addDependency(FLYWAY_GROUP_ID, artifactId("flyway-sqlserver")).and().build();
  }
}
