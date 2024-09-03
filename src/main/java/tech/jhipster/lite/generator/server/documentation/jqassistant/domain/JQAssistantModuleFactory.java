package tech.jhipster.lite.generator.server.documentation.jqassistant.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;
import static tech.jhipster.lite.module.domain.mavenplugin.MavenBuildPhase.VERIFY;

import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.javabuild.GroupId;
import tech.jhipster.lite.module.domain.javadependency.JavaDependency;
import tech.jhipster.lite.module.domain.mavenplugin.MavenPlugin;
import tech.jhipster.lite.module.domain.mavenplugin.MavenPlugin.MavenPluginOptionalBuilder;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.shared.error.domain.Assert;

public class JQAssistantModuleFactory {

  private static final String JQASSISTANT_CONFIGURATION_FILE = ".jqassistant.yml";
  private static final String SOURCE = "server/documentation/jqassistant";
  private static final String JHIPSTER_NEEDLE_JQASSISTANT_PLUGIN = "# jhipster-needle-jqassistant-plugin";
  private static final String JHIPSTER_NEEDLE_JQASSISTANT_ANALYZE_GROUP = "# jhipster-needle-jqassistant-analyze-group";
  private static final GroupId JQASSISTANT_PLUGIN_GROUP_ID = groupId("org.jqassistant.plugin");
  private static final String PROPERTIES = "properties";

  public JHipsterModule buildModule(JHipsterModuleProperties properties) {
    Assert.notNull(PROPERTIES, properties);

    //@formatter:off
    return moduleBuilder(properties)
      .files()
        .add(from(SOURCE).append(JQASSISTANT_CONFIGURATION_FILE), to(JQASSISTANT_CONFIGURATION_FILE))
        .and()
      .javaDependencies()
        .addDependencyManagement(jQAssistantContextMapperDependency())
        .and()
      .mavenPlugins()
        .pluginManagement(jQAssistantPluginManagement())
        .plugin(jQAssistantPluginBuilder().build())
        .pluginManagement(asciidoctorPluginManagement())
        .plugin(asciidoctorPluginBuilder().build())
        .and()
      .build();
    //@formatter:on
  }

  private static JavaDependency jQAssistantContextMapperDependency() {
    return JavaDependency.builder()
      .groupId(JQASSISTANT_PLUGIN_GROUP_ID)
      .artifactId("jqassistant-context-mapper-plugin")
      .versionSlug("jqassistant-context-mapper-plugin")
      .build();
  }

  private static MavenPluginOptionalBuilder jQAssistantPluginBuilder() {
    return mavenPlugin().groupId("com.buschmais.jqassistant").artifactId("jqassistant-maven-plugin");
  }

  private static MavenPlugin jQAssistantPluginManagement() {
    return jQAssistantPluginBuilder()
      .versionSlug("jqassistant")
      .addExecution(pluginExecution().goals("scan", "analyze").id("default-cli"))
      .build();
  }

  private static MavenPluginOptionalBuilder asciidoctorPluginBuilder() {
    return mavenPlugin().groupId("org.asciidoctor").artifactId("asciidoctor-maven-plugin");
  }

  private static MavenPlugin asciidoctorPluginManagement() {
    return asciidoctorPluginBuilder()
      .versionSlug("asciidoctor-maven-plugin")
      .configuration(
        """
        <backend>html5</backend>
        <attributes>
          <jqassistant-report-path>${project.build.directory}/jqassistant/jqassistant-report.xml</jqassistant-report-path>
        </attributes>
        """
      )
      .addExecution(pluginExecution().goals("process-asciidoc").id("html").phase(VERIFY))
      .addDependency(jqassistantAsciidoctorjExtensions())
      .build();
  }

  private static JavaDependency jqassistantAsciidoctorjExtensions() {
    return javaDependency()
      .groupId("org.jqassistant.tooling.asciidoctorj")
      .artifactId("jqassistant-asciidoctorj-extensions")
      .versionSlug("jqassistant-asciidoctorj-extensions")
      .build();
  }

  public JHipsterModule buildJMoleculesModule(JHipsterModuleProperties properties) {
    Assert.notNull(PROPERTIES, properties);

    //@formatter:off
    return moduleBuilder(properties)
      .javaDependencies()
        .addDependencyManagement(jQAssistantJMoleculesDependency())
        .and()
      .mandatoryReplacements()
        .in(path(JQASSISTANT_CONFIGURATION_FILE))
          .add(
            lineBeforeText(JHIPSTER_NEEDLE_JQASSISTANT_PLUGIN),
            jQAssistantPluginDeclaration(jQAssistantJMoleculesDependency())
          )
          .add(
            lineBeforeText(JHIPSTER_NEEDLE_JQASSISTANT_ANALYZE_GROUP),
            conceptOrGroupDeclaration("jmolecules-ddd:Default")
          )
          .add(
            lineBeforeText(JHIPSTER_NEEDLE_JQASSISTANT_ANALYZE_GROUP),
            conceptOrGroupDeclaration("jmolecules-hexagonal:Default")
          )
          .add(
            lineBeforeText(JHIPSTER_NEEDLE_JQASSISTANT_ANALYZE_GROUP),
            conceptOrGroupDeclaration("jmolecules-event:Default")
          )
          .and()
        .and()
      .build();
    //@formatter:on
  }

  private static JavaDependency jQAssistantJMoleculesDependency() {
    return JavaDependency.builder()
      .groupId(JQASSISTANT_PLUGIN_GROUP_ID)
      .artifactId("jqassistant-jmolecules-plugin")
      .versionSlug("jqassistant-jmolecules-plugin")
      .build();
  }

  public JHipsterModule buildSpringModule(JHipsterModuleProperties properties) {
    Assert.notNull(PROPERTIES, properties);

    //@formatter:off
    return moduleBuilder(properties)
      .javaDependencies()
        .addDependencyManagement(jQAssistantSpringDependency())
        .and()
      .mandatoryReplacements()
        .in(path(JQASSISTANT_CONFIGURATION_FILE))
          .add(
            lineBeforeText(JHIPSTER_NEEDLE_JQASSISTANT_PLUGIN),
            jQAssistantPluginDeclaration(jQAssistantSpringDependency())
          )
          .add(
            lineBeforeText(JHIPSTER_NEEDLE_JQASSISTANT_ANALYZE_GROUP),
            conceptOrGroupDeclaration("spring-boot:Default")
          )
          .and()
        .and()
      .build();
    //@formatter:on
  }

  private JavaDependency jQAssistantSpringDependency() {
    return JavaDependency.builder()
      .groupId(JQASSISTANT_PLUGIN_GROUP_ID)
      .artifactId("jqassistant-spring-plugin")
      .versionSlug("jqassistant-spring-plugin")
      .build();
  }

  private static String jQAssistantPluginDeclaration(JavaDependency pluginDependency) {
    return """
        - group-id: %s
          artifact-id: %s
          version: ${%s.version}\
    """.formatted(pluginDependency.id().groupId(), pluginDependency.id().artifactId(), pluginDependency.version().orElseThrow());
  }

  private static String conceptOrGroupDeclaration(String conceptOrGroup) {
    return "      - " + conceptOrGroup;
  }
}
