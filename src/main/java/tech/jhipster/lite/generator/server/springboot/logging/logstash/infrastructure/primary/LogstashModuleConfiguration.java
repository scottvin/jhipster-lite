package tech.jhipster.lite.generator.server.springboot.logging.logstash.infrastructure.primary;

import static tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug.LOGSTASH;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.server.springboot.logging.logstash.application.LogstashApplicationService;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleOrganization;
import tech.jhipster.lite.module.domain.resource.JHipsterModulePropertiesDefinition;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleResource;

@Configuration
class LogstashModuleConfiguration {

  @Bean
  JHipsterModuleResource logstashModule(LogstashApplicationService logstash) {
    return JHipsterModuleResource.builder()
      .slug(LOGSTASH)
      .propertiesDefinition(
        JHipsterModulePropertiesDefinition.builder().addBasePackage().addIndentation().addSpringConfigurationFormat().build()
      )
      .apiDoc("Spring Boot", "Add Logstash TCP appender")
      .organization(JHipsterModuleOrganization.SPRINGBOOT_DEPENDENCY)
      .tags("server", "logstash", "spring", "spring-boot")
      .factory(logstash::buildModule);
  }
}
