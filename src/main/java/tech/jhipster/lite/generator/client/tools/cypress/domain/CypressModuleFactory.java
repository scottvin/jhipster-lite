package tech.jhipster.lite.generator.client.tools.cypress.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;

import java.util.function.Function;
import java.util.regex.Pattern;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.file.JHipsterDestination;
import tech.jhipster.lite.module.domain.file.JHipsterSource;
import tech.jhipster.lite.module.domain.packagejson.VersionSource;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.module.domain.replacement.RegexReplacer;
import tech.jhipster.lite.shared.error.domain.Assert;

public class CypressModuleFactory {

  private static final JHipsterSource SOURCE = from("client/common/cypress");

  private static final JHipsterDestination CYPRESS_COMPONENT_TESTS = to("src/test/webapp/component");
  private static final JHipsterDestination CYPRESS_E2E_TESTS = to("src/test/webapp/e2e");

  private static final String PRIMARY_APP = "common/primary/app";
  private static final String UTILS = "utils";

  private static final String CYPRESS_COMPONENT_TESTS_EXCLUSION = "\"src/test/webapp/component/**/*.ts\"";
  private static final String CYPRESS_E2E_TESTS_EXCLUSION = "\"src/test/webapp/e2e/**/*.ts\"";
  private static final String EXCLUDE_KEY = "\"exclude\"";
  private static final RegexReplacer NEW_EXCLUSION_REPLACER = new RegexReplacer(
    (currentContent, replacement) -> !currentContent.contains(EXCLUDE_KEY),
    Pattern.compile("\\n.*}\\s*$")
  );

  private static final Function<String, RegexReplacer> EXISTING_EXCLUSION_REPLACER_PROVIDER = (String exclusion) ->
    new RegexReplacer(
      (currentContent, replacement) -> currentContent.contains(EXCLUDE_KEY) && !currentContent.contains(exclusion),
      Pattern.compile("(" + EXCLUDE_KEY + "\\s*:\\s*\\[[^]]+)]")
    );

  private static final Function<String, RegexReplacer> EMPTY_EXCLUSION_REPLACER_PROVIDER = (String exclusion) ->
    new RegexReplacer(
      (currentContent, replacement) -> currentContent.contains(EXCLUDE_KEY) && !currentContent.contains(exclusion),
      Pattern.compile("(" + EXCLUDE_KEY + "\\s*:\\s*\\[\\s*)]")
    );

  public JHipsterModule buildComponentTestsModule(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    //@formatter:off
    return commonCypressModuleBuilder(properties, CYPRESS_COMPONENT_TESTS, CYPRESS_COMPONENT_TESTS_EXCLUSION)
      .packageJson()
        .addDevDependency(packageName("start-server-and-test"), VersionSource.COMMON)
        .addScript(scriptKey("test:component"), scriptCommand("start-server-and-test start http://localhost:9000 'cypress open --e2e --config-file src/test/webapp/component/cypress-config.ts'"))
        .addScript(
          scriptKey("test:component:headless"),
          scriptCommand("start-server-and-test start http://localhost:9000 'cypress run --headless --config-file src/test/webapp/component/cypress-config.ts'")
        )
        .and()
      .context()
        .put("cypressTestDirectory", "component")
        .and()
      .build();
    //@formatter:on
  }

  public JHipsterModule buildE2ETestsModule(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    //@formatter:off
    return commonCypressModuleBuilder(properties, CYPRESS_E2E_TESTS, CYPRESS_E2E_TESTS_EXCLUSION)
      .packageJson()
        .addScript(scriptKey("e2e"), scriptCommand("cypress open --e2e --config-file src/test/webapp/e2e/cypress-config.ts"))
        .addScript(scriptKey("e2e:headless"), scriptCommand("cypress run --headless --config-file src/test/webapp/e2e/cypress-config.ts"))
        .and()
      .context()
        .put("cypressTestDirectory", "e2e")
        .and()
      .build();
    //@formatter:on
  }

  private static JHipsterModuleBuilder commonCypressModuleBuilder(
    JHipsterModuleProperties properties,
    JHipsterDestination destinationFolder,
    String tsconfigExclusion
  ) {
    //@formatter:off
    return moduleBuilder(properties)
      .packageJson()
        .addDevDependency(packageName("cypress"), VersionSource.COMMON)
        .addDevDependency(packageName("eslint-plugin-cypress"), VersionSource.COMMON)
        .and()
      .files()
        .batch(SOURCE, destinationFolder)
          .addTemplate("cypress-config.ts")
          .addFile(".eslintrc.cjs")
          .addFile("tsconfig.json")
          .and()
        .batch(SOURCE.append(PRIMARY_APP), destinationFolder.append(PRIMARY_APP))
          .addTemplate("Home.spec.ts")
          .and()
        .batch(SOURCE.append(UTILS), destinationFolder.append(UTILS))
          .addFile("Interceptor.ts")
          .addFile("DataSelector.ts")
          .and()
        .and()
      .optionalReplacements()
        .in(path("tsconfig.json"))
        .add(EXISTING_EXCLUSION_REPLACER_PROVIDER.apply(tsconfigExclusion), "$1, "+tsconfigExclusion+"]")
        .add(EMPTY_EXCLUSION_REPLACER_PROVIDER.apply(tsconfigExclusion), "$1"+tsconfigExclusion+"]")
        .add(NEW_EXCLUSION_REPLACER, newExclusionNode(properties, tsconfigExclusion))
        .and()
        .and()
      ;
    //@formatter:on
  }

  private static String newExclusionNode(JHipsterModuleProperties properties, String exclusion) {
    return new StringBuilder()
      .append(",")
      .append(LINE_BREAK)
      .append(properties.indentation().spaces())
      .append("\"exclude\": [" + exclusion + "]")
      .append(LINE_BREAK)
      .append("}")
      .toString();
  }
}
