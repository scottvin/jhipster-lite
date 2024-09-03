package tech.jhipster.lite.generator.ci.github.actions.infrastructure.primary;

import static tech.jhipster.lite.generator.slug.domain.JHLiteFeatureSlug.*;
import static tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.ci.github.actions.application.GitHubActionsApplicationService;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleOrganization;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleResource;

@Configuration
class GitHubActionsModuleConfiguration {

  private static final String CI_TAG = "ci";
  private static final String GITHUB_TAG = "github";
  private static final String CONTINUOUS_INTEGRATION_GROUP_DOC = "Continuous Integration";

  @Bean
  JHipsterModuleResource gitHubActionsMavenModule(GitHubActionsApplicationService gitHubActions) {
    return JHipsterModuleResource.builder()
      .slug(GITHUB_ACTIONS_MAVEN)
      .withoutProperties()
      .apiDoc(CONTINUOUS_INTEGRATION_GROUP_DOC, "Add GitHub Actions for Maven Build")
      .organization(JHipsterModuleOrganization.builder().feature(GITHUB_ACTIONS).addDependency(MAVEN_JAVA).build())
      .tags(CI_TAG, GITHUB_TAG)
      .factory(gitHubActions::buildGitHubActionsMavenModule);
  }

  @Bean
  JHipsterModuleResource gitHubActionsGradleModule(GitHubActionsApplicationService gitHubActions) {
    return JHipsterModuleResource.builder()
      .slug(GITHUB_ACTIONS_GRADLE)
      .withoutProperties()
      .apiDoc(CONTINUOUS_INTEGRATION_GROUP_DOC, "Add GitHub Actions for Gradle Build")
      .organization(JHipsterModuleOrganization.builder().feature(GITHUB_ACTIONS).addDependency(GRADLE_JAVA).build())
      .tags(CI_TAG, GITHUB_TAG)
      .factory(gitHubActions::buildGitHubActionsGradleModule);
  }
}
