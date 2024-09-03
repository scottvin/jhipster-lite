package tech.jhipster.lite.generator.client.svelte.core.domain;

import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class SvelteModuleFactoryTest {

  private static final SvelteModuleFactory factory = new SvelteModuleFactory();

  @Test
  void shouldCreateSvelteModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest()).build();

    JHipsterModule module = factory.buildSvelteModule(properties);

    // @formatter:off
    assertThatModuleWithFiles(module, packageJsonFile(), lintstagedFile())
      .hasFile(".gitignore")
        .containing("""
          # Svelte
          .svelte-kit/\
          """)
        .and()
      .hasFile("package.json")
        .containing(nodeDependency("svelte-navigator"))
        .containing(nodeDependency("@babel/preset-env"))
        .containing(nodeDependency("@sveltejs/adapter-static"))
        .containing(nodeDependency("@sveltejs/kit"))
        .containing(nodeDependency("@testing-library/svelte"))
        .containing(nodeDependency("@typescript-eslint/eslint-plugin"))
        .containing(nodeDependency("@typescript-eslint/parser"))
        .containing(nodeDependency("@vitest/coverage-istanbul"))
        .containing(nodeDependency("babel-plugin-transform-vite-meta-env"))
        .containing(nodeDependency("eslint"))
        .containing(nodeDependency("eslint-config-prettier"))
        .containing(nodeDependency("eslint-plugin-svelte3"))
        .containing(nodeDependency("jsdom"))
        .containing(nodeDependency("prettier"))
        .containing(nodeDependency("prettier-plugin-svelte"))
        .containing(nodeDependency("svelte"))
        .containing(nodeDependency("svelte-check"))
        .containing(nodeDependency("svelte-preprocess"))
        .containing(nodeDependency("tslib"))
        .containing(nodeDependency("typescript"))
        .containing(nodeDependency("vite"))
        .containing(nodeDependency("vitest"))
        .containing(nodeDependency("vitest-sonar-reporter"))
        .containing(nodeScript("dev", "vite dev --port 9000"))
        .containing(nodeScript("start", "vite dev --port 9000"))
        .containing(nodeScript("build", "vite build"))
        .containing(nodeScript("package", "vite package"))
        .containing(nodeScript("preview", "vite preview"))
        .containing(nodeScript("check", "svelte-check --tsconfig ./tsconfig.json"))
        .containing(nodeScript("check:watch", "svelte-check --tsconfig ./tsconfig.json --watch"))
        .containing(nodeScript("lint", "prettier --ignore-path .gitignore --check && eslint --ignore-path .gitignore ."))
        .containing(nodeScript("format", "prettier --ignore-path .gitignore --write"))
        .containing(nodeScript("test", "npm run test:watch"))
        .containing(nodeScript("test:coverage", "vitest run --coverage"))
        .containing(nodeScript("test:watch", "vitest --"))
        .containing("\"type\": \"module\"")
        .and()
      .hasFiles(".eslintignore", ".eslintrc.cjs", ".npmrc", "tsconfig.json", "svelte.config.js", "vite.config.js", "vitest.config.ts")
      .hasPrefixedFiles("src/main/webapp", "app.html", "app.d.ts")
      .hasPrefixedFiles("src/main/webapp/routes", "+page.svelte")
      .hasPrefixedFiles("src/test/webapp/unit/common/primary/app", "App.spec.ts")
      .hasPrefixedFiles("src/main/webapp/app/common/primary/app", "App.svelte")
      .hasPrefixedFiles("src/main/webapp/assets", "JHipster-Lite-neon-orange.png")
      .hasPrefixedFiles("src/main/webapp/assets", "svelte-logo.png")
      .hasFiles(".lintstagedrc.cjs")
      .doNotHaveFiles(".lintstagedrc.js");
    // @formatter:on
  }

  private ModuleFile lintstagedFile() {
    return file("src/test/resources/projects/files/.lintstagedrc.js", ".lintstagedrc.js");
  }
}
