package tech.jhipster.lite.generator.server.documentation.jqassistant.infrastructure.primary;

import static tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.server.documentation.jqassistant.application.JQAssistantApplicationService;
import tech.jhipster.lite.generator.slug.domain.JHLiteFeatureSlug;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleOrganization;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleResource;

@Configuration
class JQAssistantModuleConfiguration {

  private static final String DOCUMENTATION = "Documentation";
  private static final String SERVER_TAG = "server";
  private static final String DOCUMENTATION_TAG = "documentation";

  @Bean
  JHipsterModuleResource jQAssistantModule(JQAssistantApplicationService jqassistant) {
    return JHipsterModuleResource.builder()
      .slug(JQASSISTANT)
      .withoutProperties()
      .apiDoc(DOCUMENTATION, "Setup jQAssistant for documentation and analysis of the project")
      .organization(JHipsterModuleOrganization.builder().addDependency(JHLiteFeatureSlug.JAVA_BUILD_TOOL).build())
      .tags(SERVER_TAG, DOCUMENTATION_TAG)
      .factory(jqassistant::buildModule);
  }

  @Bean
  JHipsterModuleResource jQAssistantJMoleculesModule(JQAssistantApplicationService jqassistant) {
    return JHipsterModuleResource.builder()
      .slug(JQASSISTANT_JMOLECULES)
      .withoutProperties()
      .apiDoc(DOCUMENTATION, "Add jMolecules support for jQAssistant")
      .organization(JHipsterModuleOrganization.builder().addDependency(JQASSISTANT).addDependency(JMOLECULES).build())
      .tags(SERVER_TAG, DOCUMENTATION_TAG)
      .factory(jqassistant::buildJMoleculesModule);
  }

  @Bean
  JHipsterModuleResource jQAssistantSpringModule(JQAssistantApplicationService jqassistant) {
    return JHipsterModuleResource.builder()
      .slug(JQASSISTANT_SPRING)
      .withoutProperties()
      .apiDoc(DOCUMENTATION, "Add Spring support for jQAssistant")
      .organization(JHipsterModuleOrganization.builder().addDependency(JQASSISTANT).addDependency(SPRING_BOOT).build())
      .tags(SERVER_TAG, DOCUMENTATION_TAG)
      .factory(jqassistant::buildSpringModule);
  }
}
