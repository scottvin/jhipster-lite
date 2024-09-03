package tech.jhipster.lite.generator.server.javatool.jacoco.infrastructure.primary;

import static tech.jhipster.lite.generator.slug.domain.JHLiteFeatureSlug.*;
import static tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.server.javatool.jacoco.application.JacocoApplicationService;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleOrganization;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleResource;

@Configuration
class JacocoModuleConfiguration {

  @Bean
  JHipsterModuleResource jacocoModule(JacocoApplicationService jacoco) {
    return JHipsterModuleResource.builder()
      .slug(JACOCO)
      .withoutProperties()
      .apiDoc("Java", "Add JaCoCo for code coverage reporting")
      .organization(JHipsterModuleOrganization.builder().feature(CODE_COVERAGE_JAVA).addDependency(JAVA_BUILD_TOOL).build())
      .tags("server", "tools", "coverage")
      .factory(jacoco::buildJacocoModule);
  }

  @Bean
  JHipsterModuleResource jacocoWithMinCoverageCheckModule(JacocoApplicationService jacoco) {
    return JHipsterModuleResource.builder()
      .slug(JACOCO_WITH_MIN_COVERAGE_CHECK)
      .withoutProperties()
      .apiDoc("Java", "Add JaCoCo for code coverage reporting and 100% coverage check")
      .organization(JHipsterModuleOrganization.builder().feature(CODE_COVERAGE_JAVA).addDependency(JAVA_BUILD_TOOL).build())
      .tags("server", "tools", "coverage")
      .factory(jacoco::buildJacocoWithMinCoverageCheckModule);
  }
}
