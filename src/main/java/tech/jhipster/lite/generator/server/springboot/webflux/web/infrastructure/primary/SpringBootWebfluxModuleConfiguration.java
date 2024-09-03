package tech.jhipster.lite.generator.server.springboot.webflux.web.infrastructure.primary;

import static tech.jhipster.lite.generator.slug.domain.JHLiteFeatureSlug.SPRING_SERVER;
import static tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.server.springboot.webflux.web.application.SpringBootWebfluxApplicationService;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleOrganization;
import tech.jhipster.lite.module.domain.resource.JHipsterModulePropertiesDefinition;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleResource;

@Configuration
class SpringBootWebfluxModuleConfiguration {

  @Bean
  JHipsterModuleResource springBootWebfluxModule(SpringBootWebfluxApplicationService webflux) {
    return JHipsterModuleResource.builder()
      .slug(SPRING_BOOT_WEBFLUX_EMPTY)
      .propertiesDefinition(properties())
      .apiDoc("Spring Boot - Webflux", "Empty module: do not use alone. You should add module Spring Boot Webflux Netty")
      .organization(JHipsterModuleOrganization.builder().feature(SPRING_SERVER).addDependency(SPRING_BOOT).build())
      .tags("server", "webflux", "netty")
      .factory(webflux::buildEmptyModule);
  }

  @Bean
  JHipsterModuleResource springBootWebfluxNettyModule(SpringBootWebfluxApplicationService webflux) {
    return JHipsterModuleResource.builder()
      .slug(SPRING_BOOT_WEBFLUX_NETTY)
      .propertiesDefinition(properties())
      .apiDoc("Spring Boot - Webflux", "Add Spring Boot Webflux Netty")
      .organization(JHipsterModuleOrganization.builder().addDependency(SPRING_BOOT_WEBFLUX_EMPTY).build())
      .tags("server", "webflux")
      .factory(webflux::buildNettyModule);
  }

  private static JHipsterModulePropertiesDefinition properties() {
    return JHipsterModulePropertiesDefinition.builder()
      .addBasePackage()
      .addIndentation()
      .addServerPort()
      .addSpringConfigurationFormat()
      .build();
  }
}
