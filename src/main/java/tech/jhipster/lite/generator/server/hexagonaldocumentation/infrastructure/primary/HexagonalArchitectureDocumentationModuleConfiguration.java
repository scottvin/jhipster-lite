package tech.jhipster.lite.generator.server.hexagonaldocumentation.infrastructure.primary;

import static tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.server.hexagonaldocumentation.application.HexagonalArchitectureDocumentationApplicationService;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleResource;

@Configuration
class HexagonalArchitectureDocumentationModuleConfiguration {

  @Bean
  JHipsterModuleResource hexagonalArchitectureDocumentationModule(
    HexagonalArchitectureDocumentationApplicationService hexagonalArchitectureDocumentations
  ) {
    return JHipsterModuleResource.builder()
      .slug(APPLICATION_SERVICE_HEXAGONAL_ARCHITECTURE_DOCUMENTATION)
      .withoutProperties()
      .apiDoc("Documentation", "Add documentation for hexagonal architecture")
      .standalone()
      .tags("server", "documentation")
      .factory(hexagonalArchitectureDocumentations::buildModule);
  }
}
