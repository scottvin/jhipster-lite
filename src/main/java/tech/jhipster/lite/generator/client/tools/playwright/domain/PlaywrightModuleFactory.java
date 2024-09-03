package tech.jhipster.lite.generator.client.tools.playwright.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;

import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.file.JHipsterDestination;
import tech.jhipster.lite.module.domain.file.JHipsterSource;
import tech.jhipster.lite.module.domain.packagejson.VersionSource;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.shared.error.domain.Assert;

public class PlaywrightModuleFactory {

  private static final JHipsterSource SOURCE = from("client/common/playwright");

  private static final JHipsterDestination WEBAPP_COMPONENT_TESTS = to("src/test/webapp/component/");
  private static final JHipsterDestination WEBAPP_E2E_TESTS = to("src/test/webapp/e2e/");
  private static final String PLAYWRIGHT_TESTS = "common/primary/app";

  public JHipsterModule buildComponentTestsModule(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    //@formatter:off
    return commonPlaywrightModuleBuilder(properties, WEBAPP_COMPONENT_TESTS)
      .packageJson()
        .addDevDependency(packageName("start-server-and-test"), VersionSource.COMMON)
        .addScript(scriptKey("test:component"), scriptCommand("start-server-and-test start http://localhost:9000 'playwright test --ui --config src/test/webapp/component/playwright.config.ts'"))
        .addScript(scriptKey("test:component:headless"), scriptCommand("start-server-and-test start http://localhost:9000 'playwright test --config src/test/webapp/component/playwright.config.ts'"))
        .and()
      .context()
        .put("reportSubDirectory", "component-tests")
        .put("resultsSubDirectory", "component-tests")
        .and()
      .build();
    //@formatter:on
  }

  public JHipsterModule buildE2ETestsModule(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    //@formatter:off
    return commonPlaywrightModuleBuilder(properties, WEBAPP_E2E_TESTS)
      .packageJson()
        .addScript(scriptKey("e2e"), scriptCommand("playwright test --ui --config src/test/webapp/e2e/playwright.config.ts"))
        .addScript(scriptKey("e2e:headless"), scriptCommand("playwright test --config src/test/webapp/e2e/playwright.config.ts"))
        .and()
      .context()
        .put("reportSubDirectory", "e2e-tests")
        .put("resultsSubDirectory", "e2e-tests")
        .and()
      .build();
    //@formatter:on
  }

  private static JHipsterModuleBuilder commonPlaywrightModuleBuilder(
    JHipsterModuleProperties properties,
    JHipsterDestination destinationFolder
  ) {
    //@formatter:off
    return moduleBuilder(properties)
      .packageJson()
        .addDevDependency(packageName("@playwright/test"), VersionSource.COMMON)
        .and()
      .files()
        .add(SOURCE.template("playwright.config.ts"), destinationFolder.append("playwright.config.ts"))
        .batch(SOURCE.append(PLAYWRIGHT_TESTS), destinationFolder.append(PLAYWRIGHT_TESTS))
          .addTemplate("Home.spec.ts")
          .addFile("Home-Page.ts")
          .and()
      .and();
    //@formatter:on
  }
}
