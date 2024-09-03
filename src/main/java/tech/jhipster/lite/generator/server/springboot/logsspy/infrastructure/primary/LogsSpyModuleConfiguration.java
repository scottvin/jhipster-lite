package tech.jhipster.lite.generator.server.springboot.logsspy.infrastructure.primary;

import static tech.jhipster.lite.generator.slug.domain.JHLiteFeatureSlug.SPRING_SERVER;
import static tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug.LOGS_SPY;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.server.springboot.logsspy.application.LogsSpyApplicationService;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleOrganization;
import tech.jhipster.lite.module.domain.resource.JHipsterModulePropertiesDefinition;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleResource;

@Configuration
class LogsSpyModuleConfiguration {

  @Bean
  JHipsterModuleResource logSpyModule(LogsSpyApplicationService logSpy) {
    return JHipsterModuleResource.builder()
      .slug(LOGS_SPY)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addBasePackage().addProjectBaseName().addIndentation().build())
      .apiDoc("Spring Boot", "Add LogsSpy JUnit5 extension to project")
      .organization(JHipsterModuleOrganization.builder().addDependency(SPRING_SERVER).build())
      .tags("server", "test", "logback", "junit-extension")
      .factory(logSpy::buildModule);
  }
}
