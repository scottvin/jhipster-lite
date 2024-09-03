package tech.jhipster.lite.generator.buildtool.gradle.infrastructure.primary;

import static tech.jhipster.lite.generator.slug.domain.JHLiteFeatureSlug.JAVA_BUILD_TOOL;
import static tech.jhipster.lite.generator.slug.domain.JHLiteFeatureSlug.JAVA_BUILD_TOOL_WRAPPER;
import static tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.buildtool.gradle.application.GradleApplicationService;
import tech.jhipster.lite.module.domain.resource.*;

@Configuration
class GradleModuleConfiguration {

  @Bean
  JHipsterModuleResource gradleModule(GradleApplicationService gradle) {
    return JHipsterModuleResource.builder()
      .slug(GRADLE_JAVA)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addBasePackage().addProjectBaseName().addProjectName().build())
      .apiDoc("Build Tool", "Init Gradle project with kotlin DSL")
      .organization(JHipsterModuleOrganization.builder().feature(JAVA_BUILD_TOOL).addDependency(INIT).build())
      .tags("buildtool", "test")
      .factory(gradle::buildGradleModule);
  }

  @Bean
  JHipsterModuleResource gradleWrapperModule(GradleApplicationService gradle) {
    return JHipsterModuleResource.builder()
      .slug(GRADLE_WRAPPER)
      .withoutProperties()
      .apiDoc("Build Tool", "Add gradle wrapper")
      .organization(JHipsterModuleOrganization.builder().feature(JAVA_BUILD_TOOL_WRAPPER).addDependency(GRADLE_JAVA).build())
      .tags("buildtool", "test")
      .factory(gradle::buildGradleWrapperModule);
  }
}
