package tech.jhipster.lite.generator.server.springboot.docker.infrastructure.primary;

import static tech.jhipster.lite.generator.slug.domain.JHLiteFeatureSlug.*;
import static tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.server.springboot.docker.application.SpringBootDockerApplicationService;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleOrganization;
import tech.jhipster.lite.module.domain.resource.JHipsterModulePropertiesDefinition;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleResource;

@Configuration
class SpringBootDockerModuleConfiguration {

  private static final String SPRING_BOOT_TOOLS_GROUP = "Spring Boot - Tools";
  private static final String SERVER_TAG = "server";
  private static final String SPRING_TAG = "spring";
  private static final String DOCKER_TAG = "docker";
  private static final String JIB_TAG = "jib";
  private static final String SPRING_BOOT_TAG = "spring-boot";

  @Bean
  JHipsterModuleResource jibModule(SpringBootDockerApplicationService springBootDocker) {
    return JHipsterModuleResource.builder()
      .slug(JIB)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addBasePackage().addProjectBaseName().addServerPort().build())
      .apiDoc(SPRING_BOOT_TOOLS_GROUP, "Add Docker image building with Jib")
      .organization(JHipsterModuleOrganization.builder().addDependency(JAVA_BUILD_TOOL).build())
      .tags(SERVER_TAG, SPRING_TAG, SPRING_BOOT_TAG, JIB_TAG)
      .factory(springBootDocker::buildJibModule);
  }

  @Bean
  JHipsterModuleResource dockerFileMavenModule(SpringBootDockerApplicationService springBootDocker) {
    return JHipsterModuleResource.builder()
      .slug(DOCKERFILE_MAVEN)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addServerPort().build())
      .apiDoc(SPRING_BOOT_TOOLS_GROUP, "Add Dockerfile with maven commands")
      .organization(JHipsterModuleOrganization.builder().feature(DOCKERFILE).addDependency(MAVEN_WRAPPER).build())
      .tags(SERVER_TAG, SPRING_TAG, SPRING_BOOT_TAG, DOCKER_TAG)
      .factory(springBootDocker::buildDockerFileMavenModule);
  }

  @Bean
  JHipsterModuleResource dockerFileGradleModule(SpringBootDockerApplicationService springBootDocker) {
    return JHipsterModuleResource.builder()
      .slug(DOCKERFILE_GRADLE)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addServerPort().build())
      .apiDoc(SPRING_BOOT_TOOLS_GROUP, "Add Dockerfile with gradle commands")
      .organization(JHipsterModuleOrganization.builder().feature(DOCKERFILE).addDependency(GRADLE_WRAPPER).build())
      .tags(SERVER_TAG, SPRING_TAG, SPRING_BOOT_TAG, DOCKER_TAG)
      .factory(springBootDocker::buildDockerFileGradleModule);
  }
}
