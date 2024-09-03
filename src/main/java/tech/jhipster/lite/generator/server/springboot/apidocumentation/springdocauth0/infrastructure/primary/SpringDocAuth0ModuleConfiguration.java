package tech.jhipster.lite.generator.server.springboot.apidocumentation.springdocauth0.infrastructure.primary;

import static tech.jhipster.lite.generator.slug.domain.JHLiteFeatureSlug.OAUTH_PROVIDER_SPRINGDOC;
import static tech.jhipster.lite.generator.slug.domain.JHLiteFeatureSlug.SPRINGDOC;
import static tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug.SPRINGDOC_OAUTH_2_AUTH_0;
import static tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug.SPRING_BOOT_OAUTH_2_AUTH_0;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.server.springboot.apidocumentation.springdocauth0.application.SpringDocAuth0ApplicationService;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleOrganization;
import tech.jhipster.lite.module.domain.resource.JHipsterModulePropertiesDefinition;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleResource;

@Configuration
class SpringDocAuth0ModuleConfiguration {

  @Bean
  JHipsterModuleResource springDocAuth0Module(SpringDocAuth0ApplicationService springdocAuth0) {
    return JHipsterModuleResource.builder()
      .slug(SPRINGDOC_OAUTH_2_AUTH_0)
      .propertiesDefinition(
        JHipsterModulePropertiesDefinition.builder().addBasePackage().addIndentation().addSpringConfigurationFormat().build()
      )
      .apiDoc("Spring Boot - API Documentation", "Add Auth0 authentication for springdoc")
      .organization(
        JHipsterModuleOrganization.builder()
          .feature(OAUTH_PROVIDER_SPRINGDOC)
          .addDependency(SPRINGDOC)
          .addDependency(SPRING_BOOT_OAUTH_2_AUTH_0)
          .build()
      )
      .tags("server", "swagger", "springdoc", "auth0")
      .factory(springdocAuth0::buildModule);
  }
}
