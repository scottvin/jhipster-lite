package tech.jhipster.lite.generator.server.springboot.database.mysql.domain;

import static org.mockito.Mockito.when;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.assertThatModuleWithFiles;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.pomFile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.docker.DockerImageVersion;
import tech.jhipster.lite.module.domain.docker.DockerImages;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
@ExtendWith(MockitoExtension.class)
class MySQLModuleFactoryTest {

  @Mock
  private DockerImages dockerImages;

  @InjectMocks
  private MySQLModuleFactory factory;

  @Test
  void shouldCreateModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest())
      .basePackage("tech.jhipster.jhlitest")
      .projectBaseName("myapp")
      .build();

    when(dockerImages.get("mysql")).thenReturn(new DockerImageVersion("mysql", "0.0.0"));

    JHipsterModule module = factory.buildModule(properties);

    assertThatModuleWithFiles(module, pomFile())
      .hasFile("documentation/mysql.md")
      .and()
      .hasPrefixedFiles("src/main/java/tech/jhipster/jhlitest/wire/mysql/infrastructure/secondary", "DatabaseConfiguration.java")
      .hasPrefixedFiles("src/main/docker", "mysql.yml")
      .hasFile("pom.xml")
      .containing("<groupId>org.springframework.boot</groupId>")
      .containing("<artifactId>spring-boot-starter-data-jpa</artifactId>")
      .containing(
        """
            <dependency>
              <groupId>com.mysql</groupId>
              <artifactId>mysql-connector-j</artifactId>
              <scope>runtime</scope>
            </dependency>
        """
      )
      .containing("<groupId>com.zaxxer</groupId>")
      .containing("<artifactId>HikariCP</artifactId>")
      .containing("<groupId>org.hibernate.orm</groupId>")
      .containing("<artifactId>hibernate-core</artifactId>")
      .containing("<groupId>org.testcontainers</groupId>")
      .containing("<artifactId>mysql</artifactId>")
      .and()
      .hasFile("src/main/resources/config/application.yml")
      .containing(
        // language=yaml
        """
        spring:
          data:
            jpa:
              repositories:
                bootstrap-mode: deferred
          datasource:
            driver-class-name: com.mysql.cj.jdbc.Driver
            hikari:
              auto-commit: false
              poolName: Hikari
            password: ''
            type: com.zaxxer.hikari.HikariDataSource
            url: jdbc:mysql://localhost:3306/myapp
            username: root
          jpa:
            hibernate:
              ddl-auto: none
              naming:
                implicit-strategy: org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
                physical-strategy: org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
            open-in-view: false
            properties:
              hibernate:
                connection:
                  provider_disables_autocommit: true
                generate_statistics: false
                jdbc:
                  batch_size: 25
                  time_zone: UTC
                order_inserts: true
                order_updates: true
                query:
                  fail_on_pagination_over_collection_fetch: true
                  in_clause_parameter_padding: true
        """
      )
      .and()
      .hasFile("src/test/resources/config/application-test.yml")
      .containing(
        // language=yaml
        """
        spring:
          datasource:
            driver-class-name: org.testcontainers.jdbc.ContainerDatabaseDriver
            hikari:
              maximum-pool-size: 2
            password: ''
            url: jdbc:tc:mysql:0.0.0:///myapp
            username: myapp
        """
      );
  }
}
