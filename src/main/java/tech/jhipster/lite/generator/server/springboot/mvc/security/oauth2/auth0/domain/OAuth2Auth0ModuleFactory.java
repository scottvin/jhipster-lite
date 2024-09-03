package tech.jhipster.lite.generator.server.springboot.mvc.security.oauth2.auth0.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;

import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.file.JHipsterSource;
import tech.jhipster.lite.module.domain.javaproperties.PropertyValue;
import tech.jhipster.lite.module.domain.javaproperties.SpringProfile;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.shared.error.domain.Assert;

public class OAuth2Auth0ModuleFactory {

  private static final JHipsterSource SOURCE = from("server/springboot/mvc/security/oauth2/auth0");

  private static final SpringProfile AUTH0_SPRING_PROFILE = new SpringProfile("auth0");

  private static final String CLIENT_ID_PROPERTY = "auth0ClientId";
  private static final String AUTH0_DOMAIN_PROPERTY = "auth0Domain";
  private static final String AUTH0_SHELL_SCRIPT = "auth0.sh";

  public JHipsterModule buildModule(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    //@formatter:off
    return moduleBuilder(properties)
      .documentation(documentationTitle("Auth0"), SOURCE.template("documentation/auth0.md.mustache"))
      .gitIgnore()
        .comment("OAuth 2.0")
        .pattern(AUTH0_SHELL_SCRIPT)
        .and()
      .files()
        .add(SOURCE.file(AUTH0_SHELL_SCRIPT), to(AUTH0_SHELL_SCRIPT))
        .and()
      .springMainProperties(AUTH0_SPRING_PROFILE)
        .set(propertyKey("application.security.oauth2.audience"), audience(properties))
        .set(propertyKey("spring.security.oauth2.client.provider.oidc.issuer-uri"), issuerUri(properties))
        .set(propertyKey("spring.security.oauth2.client.registration.oidc.client-id"), clientId(properties))
        .and()
      .build();
    //@formatter:on
  }

  private static PropertyValue audience(JHipsterModuleProperties properties) {
    return propertyValue(
      "application.security.oauth2.audience=account",
      "api://default",
      "https://" + properties.getString(AUTH0_DOMAIN_PROPERTY) + "/api/v2/"
    );
  }

  private static PropertyValue issuerUri(JHipsterModuleProperties properties) {
    return propertyValue("https://" + properties.getString(AUTH0_DOMAIN_PROPERTY) + "/");
  }

  private static PropertyValue clientId(JHipsterModuleProperties properties) {
    return propertyValue(properties.getString(CLIENT_ID_PROPERTY));
  }
}
