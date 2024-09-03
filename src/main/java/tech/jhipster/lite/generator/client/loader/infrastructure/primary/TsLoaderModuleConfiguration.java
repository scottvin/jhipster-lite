package tech.jhipster.lite.generator.client.loader.infrastructure.primary;

import static tech.jhipster.lite.generator.slug.domain.JHLiteFeatureSlug.CLIENT_CORE;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.client.loader.application.TsLoaderModuleApplicationService;
import tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleOrganization;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleResource;

@Configuration
class TsLoaderModuleConfiguration {

  @Bean
  JHipsterModuleResource tsLoaderModule(TsLoaderModuleApplicationService tsLoader) {
    return JHipsterModuleResource.builder()
      .slug(JHLiteModuleSlug.TS_LOADER)
      .withoutProperties()
      .apiDoc("Frontend", "Helper class to represent loading states")
      .organization(JHipsterModuleOrganization.builder().addDependency(CLIENT_CORE).build())
      .tags("client")
      .factory(tsLoader::buildModule);
  }
}
