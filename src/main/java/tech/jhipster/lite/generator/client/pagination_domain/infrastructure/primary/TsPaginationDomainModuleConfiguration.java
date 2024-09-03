package tech.jhipster.lite.generator.client.pagination_domain.infrastructure.primary;

import static tech.jhipster.lite.generator.slug.domain.JHLiteFeatureSlug.CLIENT_CORE;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.client.pagination_domain.application.TsPaginationDomainApplicationService;
import tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleOrganization;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleResource;

@Configuration
class TsPaginationDomainModuleConfiguration {

  @Bean
  JHipsterModuleResource tsPaginationDomainModule(TsPaginationDomainApplicationService tsPaginationDomain) {
    return JHipsterModuleResource.builder()
      .slug(JHLiteModuleSlug.TS_PAGINATION_DOMAIN)
      .withoutProperties()
      .apiDoc("Pagination", "Add webapp domain for pagination")
      .organization(JHipsterModuleOrganization.builder().addDependency(CLIENT_CORE).build())
      .tags("client", "webapp", "frontend")
      .factory(tsPaginationDomain::buildModule);
  }
}
