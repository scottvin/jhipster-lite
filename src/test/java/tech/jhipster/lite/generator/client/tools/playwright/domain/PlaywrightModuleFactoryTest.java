package tech.jhipster.lite.generator.client.tools.playwright.domain;

import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class PlaywrightModuleFactoryTest {

  private static final PlaywrightModuleFactory factory = new PlaywrightModuleFactory();

  @Test
  void shouldBuildComponentTestsModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest()).build();

    JHipsterModule module = factory.buildComponentTestsModule(properties);

    assertThatModuleWithFiles(module, packageJsonFile())
      .hasFile("package.json")
      .containing(nodeDependency("start-server-and-test"))
      .containing(nodeDependency("@playwright/test"))
      .containing(
        nodeScript(
          "test:component",
          "start-server-and-test start http://localhost:9000 'playwright test --ui --config src/test/webapp/component/playwright.config.ts'"
        )
      )
      .containing(
        nodeScript(
          "test:component:headless",
          "start-server-and-test start http://localhost:9000 'playwright test --config src/test/webapp/component/playwright.config.ts'"
        )
      )
      .and()
      .hasFile("src/test/webapp/component/playwright.config.ts")
      .containing("baseURL: process.env.URL || 'http://localhost:9000',")
      .containing("outputFolder: '../../../../target/playwright-report/component-tests'")
      .containing("outputDir: '../../../../target/playwright-results/component-tests'")
      .and()
      .hasPrefixedFiles("src/test/webapp/component/common/primary/app", "Home.spec.ts", "Home-Page.ts");
  }

  @Test
  void shouldBuildE2ETestsModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest()).build();

    JHipsterModule module = factory.buildE2ETestsModule(properties);

    assertThatModuleWithFiles(module, packageJsonFile())
      .hasFile("package.json")
      .containing(nodeDependency("@playwright/test"))
      .containing(nodeScript("e2e", "playwright test --ui --config src/test/webapp/e2e/playwright.config.ts"))
      .containing(nodeScript("e2e:headless", "playwright test --config src/test/webapp/e2e/playwright.config.ts"))
      .and()
      .hasFile("src/test/webapp/e2e/playwright.config.ts")
      .containing("baseURL: process.env.URL || 'http://localhost:9000',")
      .containing("outputFolder: '../../../../target/playwright-report/e2e-tests'")
      .containing("outputDir: '../../../../target/playwright-results/e2e-tests'")
      .and()
      .hasPrefixedFiles("src/test/webapp/e2e/common/primary/app", "Home.spec.ts", "Home-Page.ts");
  }
}
