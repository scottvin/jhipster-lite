package tech.jhipster.lite.generator.server.springboot.mvc.security.common.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;

import tech.jhipster.lite.module.domain.file.JHipsterDestination;
import tech.jhipster.lite.module.domain.file.JHipsterSource;
import tech.jhipster.lite.module.domain.javabuild.GroupId;
import tech.jhipster.lite.module.domain.javadependency.JavaDependency;
import tech.jhipster.lite.module.domain.javadependency.JavaDependencyScope;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.module.domain.replacement.TextNeedleBeforeReplacer;
import tech.jhipster.lite.shared.error.domain.Assert;

public final class AuthenticationModulesFactory {

  private static final String AUTHENTICATION_DESTINATION = "shared/authentication";

  private static final JHipsterSource SOURCE = from("server/springboot/mvc/security/common");

  private static final JHipsterSource MAIN_SOURCE = SOURCE.append("main");
  private static final JHipsterSource TEST_SOURCE = SOURCE.append("test");

  private static final GroupId SPRING_GROUP = groupId("org.springframework.boot");

  private static final String APPLICATION = "application";
  private static final String DOMAIN = "domain";
  private static final String PRIMARY = "infrastructure/primary";

  private static final TextNeedleBeforeReplacer IMPORT_NEEDLE = lineBeforeText(
    "import org.springframework.boot.test.context.SpringBootTest;"
  );
  private static final TextNeedleBeforeReplacer ANNOTATION_NEEDLE = lineBeforeText("@Target(ElementType.TYPE)");

  private AuthenticationModulesFactory() {}

  public static JHipsterModuleBuilder authenticationModuleBuilder(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    String packagePath = properties.basePackage().path();
    JHipsterDestination mainDestination = toSrcMainJava().append(packagePath).append(AUTHENTICATION_DESTINATION);
    JHipsterDestination testDestination = toSrcTestJava().append(packagePath).append(AUTHENTICATION_DESTINATION);

    //@formatter:off
    return moduleBuilder(properties)
      .context()
        .put("baseName", properties.projectBaseName().capitalized())
        .and()
      .javaDependencies()
        .addDependency(SPRING_GROUP, artifactId("spring-boot-starter-security"))
        .addDependency(springSecurityTest())
        .and()
      .files()
        .add(MAIN_SOURCE.template("package-info.java"), mainDestination.append("package-info.java"))
          .batch(MAIN_SOURCE.append(DOMAIN), mainDestination.append(DOMAIN))
          .addTemplate("Role.java")
          .addTemplate("Roles.java")
          .addTemplate("Username.java")
          .and()
        .batch(MAIN_SOURCE.append(APPLICATION), mainDestination.append(APPLICATION))
          .addTemplate("AuthenticationException.java")
          .addTemplate("NotAuthenticatedUserException.java")
          .addTemplate("UnknownAuthenticationException.java")
          .and()
        .add(MAIN_SOURCE.append(PRIMARY).template("AuthenticationExceptionAdvice.java"), mainDestination.append(PRIMARY).append("AuthenticationExceptionAdvice.java"))
        .batch(TEST_SOURCE.append(DOMAIN), testDestination.append(DOMAIN))
          .addTemplate("RolesTest.java")
          .addTemplate("RoleTest.java")
          .addTemplate("UsernameTest.java")
          .and()
        .batch(TEST_SOURCE.append(PRIMARY), testDestination.append(PRIMARY))
          .addTemplate("AccountExceptionResource.java")
          .addTemplate("AuthenticationExceptionAdviceIT.java")
          .and()
        .and()
      .mandatoryReplacements()
        .in(path("src/test/java").append(properties.packagePath()).append("IntegrationTest.java"))
          .add(IMPORT_NEEDLE, "import org.springframework.security.test.context.support.WithMockUser;")
          .add(ANNOTATION_NEEDLE, "@WithMockUser")
          .and()
      .and();
    //@formatter:on
  }

  private static JavaDependency springSecurityTest() {
    return JavaDependency.builder()
      .groupId("org.springframework.security")
      .artifactId("spring-security-test")
      .scope(JavaDependencyScope.TEST)
      .build();
  }
}
