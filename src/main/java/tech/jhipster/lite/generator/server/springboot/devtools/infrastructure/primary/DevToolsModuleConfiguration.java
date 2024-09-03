package tech.jhipster.lite.generator.server.springboot.devtools.infrastructure.primary;

import static tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug.SPRING_BOOT_DEVTOOLS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.server.springboot.devtools.application.DevToolsApplicationService;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleOrganization;
import tech.jhipster.lite.module.domain.resource.JHipsterModulePropertiesDefinition;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleResource;

@Configuration
class DevToolsModuleConfiguration {

  @Bean
  JHipsterModuleResource devTools(DevToolsApplicationService devtools) {
    return JHipsterModuleResource.builder()
      .slug(SPRING_BOOT_DEVTOOLS)
      .propertiesDefinition(
        JHipsterModulePropertiesDefinition.builder()
          .addBasePackage()
          .addProjectBaseName()
          .addIndentation()
          .addSpringConfigurationFormat()
          .build()
      )
      .apiDoc("Spring Boot - Tools", "Add Spring Boot devtools.")
      .organization(JHipsterModuleOrganization.SPRINGBOOT_DEPENDENCY)
      .tags("server", "spring", "spring-boot", "devtools")
      .factory(devtools::buildModule);
  }
}
