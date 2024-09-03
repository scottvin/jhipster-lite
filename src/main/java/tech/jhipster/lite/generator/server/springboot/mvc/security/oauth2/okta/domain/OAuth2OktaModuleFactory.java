package tech.jhipster.lite.generator.server.springboot.mvc.security.oauth2.okta.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;

import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.file.JHipsterSource;
import tech.jhipster.lite.module.domain.javaproperties.PropertyValue;
import tech.jhipster.lite.module.domain.javaproperties.SpringProfile;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.shared.error.domain.Assert;

public class OAuth2OktaModuleFactory {

  private static final JHipsterSource SOURCE = from("server/springboot/mvc/security/oauth2/okta");

  private static final SpringProfile OKTA_SPRING_PROFILE = new SpringProfile("okta");

  private static final String OKTA_CLIENT_ID_PROPERTY = "oktaClientId";
  private static final String OKTA_DOMAIN_PROPERTY = "oktaDomain";
  private static final String OKTA_SHELL_SCRIPT = "okta.sh";

  public JHipsterModule buildModule(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    //@formatter:off
    return moduleBuilder(properties)
      .documentation(documentationTitle("Okta"), SOURCE.template("documentation/okta.md"))
      .gitIgnore()
        .comment("OAuth 2.0")
        .pattern(OKTA_SHELL_SCRIPT)
        .and()
      .files()
        .add(SOURCE.file(OKTA_SHELL_SCRIPT), to(OKTA_SHELL_SCRIPT))
        .add(SOURCE.file("documentation/images/security-add-claim.png"), to("documentation/images/security-add-claim.png"))
        .and()
      .springMainProperties(OKTA_SPRING_PROFILE)
        .set(propertyKey("spring.security.oauth2.client.provider.oidc.issuer-uri"), issuerUri(properties))
        .set(propertyKey("spring.security.oauth2.client.registration.oidc.client-id"), clientId(properties))
        .and()
      .build();
    //@formatter:on
  }

  private static PropertyValue issuerUri(JHipsterModuleProperties properties) {
    return propertyValue("https://" + properties.getString(OKTA_DOMAIN_PROPERTY) + "/oauth2/default");
  }

  private static PropertyValue clientId(JHipsterModuleProperties properties) {
    return propertyValue(properties.getString(OKTA_CLIENT_ID_PROPERTY));
  }
}
