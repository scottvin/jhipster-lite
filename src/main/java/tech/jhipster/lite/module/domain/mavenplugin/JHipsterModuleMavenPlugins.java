package tech.jhipster.lite.module.domain.mavenplugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import tech.jhipster.lite.module.domain.javabuild.command.*;
import tech.jhipster.lite.module.domain.javabuildprofile.BuildProfileId;
import tech.jhipster.lite.module.domain.javadependency.JavaDependenciesVersions;
import tech.jhipster.lite.module.domain.javadependency.JavaDependency;
import tech.jhipster.lite.module.domain.javadependency.JavaDependencyVersion;
import tech.jhipster.lite.module.domain.javadependency.ProjectJavaDependencies;
import tech.jhipster.lite.shared.error.domain.Assert;

public final class JHipsterModuleMavenPlugins {

  private final Collection<MavenPlugin> pluginsManagement;
  private final Collection<MavenPlugin> plugins;

  private JHipsterModuleMavenPlugins(JHipsterModuleMavenPluginsBuilder<?> builder) {
    pluginsManagement = builder.pluginsManagement;
    plugins = builder.plugins;
  }

  public static <T> JHipsterModuleMavenPluginsBuilder<T> builder(T parentModuleBuilder) {
    return new JHipsterModuleMavenPluginsBuilder<>(parentModuleBuilder);
  }

  public JavaBuildCommands buildChanges(JavaDependenciesVersions versions, ProjectJavaDependencies projectJavaDependencies) {
    return buildChanges(versions, projectJavaDependencies, Optional.empty());
  }

  public JavaBuildCommands buildChanges(
    JavaDependenciesVersions versions,
    ProjectJavaDependencies projectJavaDependencies,
    BuildProfileId buildProfile
  ) {
    Assert.notNull("buildProfile", buildProfile);
    return buildChanges(versions, projectJavaDependencies, Optional.of(buildProfile));
  }

  private JavaBuildCommands buildChanges(
    JavaDependenciesVersions versions,
    ProjectJavaDependencies projectJavaDependencies,
    Optional<BuildProfileId> buildProfile
  ) {
    Assert.notNull("versions", versions);
    Assert.notNull("projectJavaDependencies", projectJavaDependencies);

    Stream<JavaBuildCommand> managementCommands = pluginsManagement
      .stream()
      .map(toAddMavenPlugin(versions, projectJavaDependencies, buildProfile, AddMavenPluginManagement::builder));
    Stream<JavaBuildCommand> pluginsCommands = plugins
      .stream()
      .map(toAddMavenPlugin(versions, projectJavaDependencies, buildProfile, AddDirectMavenPlugin::builder));

    return new JavaBuildCommands(Stream.concat(managementCommands, pluginsCommands).toList());
  }

  private static <C extends AddMavenPlugin> Function<MavenPlugin, C> toAddMavenPlugin(
    JavaDependenciesVersions versions,
    ProjectJavaDependencies projectDependencies,
    Optional<BuildProfileId> buildProfile,
    Supplier<AddMavenPluginPluginBuilder<C>> builderFactory
  ) {
    return plugin -> {
      AddMavenPluginOptionalBuilder<C> commandBuilder = builderFactory.get().plugin(plugin);
      buildProfile.ifPresent(commandBuilder::buildProfile);
      plugin
        .dependencies()
        .stream()
        .flatMap(toDependencyVersion(versions, projectDependencies))
        .forEach(commandBuilder::addDependencyVersion);
      plugin.versionSlug().map(versions::get).ifPresent(commandBuilder::pluginVersion);
      return commandBuilder.build();
    };
  }

  private static Function<JavaDependency, Stream<JavaDependencyVersion>> toDependencyVersion(
    JavaDependenciesVersions versions,
    ProjectJavaDependencies projectDependencies
  ) {
    return dependency -> dependency.version().flatMap(JavaDependency.toVersion(versions, projectDependencies)).stream();
  }

  public static final class JHipsterModuleMavenPluginsBuilder<T> {

    private final T parentModuleBuilder;
    private final Collection<MavenPlugin> pluginsManagement = new ArrayList<>();
    private final Collection<MavenPlugin> plugins = new ArrayList<>();

    private JHipsterModuleMavenPluginsBuilder(T parentModuleBuilder) {
      Assert.notNull("parentModuleBuilder", parentModuleBuilder);

      this.parentModuleBuilder = parentModuleBuilder;
    }

    public JHipsterModuleMavenPluginsBuilder<T> pluginManagement(MavenPlugin pluginManagement) {
      Assert.notNull("pluginManagement", pluginManagement);

      pluginsManagement.add(pluginManagement);

      return this;
    }

    public JHipsterModuleMavenPluginsBuilder<T> plugin(MavenPlugin plugin) {
      Assert.notNull("plugin", plugin);

      plugins.add(plugin);

      return this;
    }

    public T and() {
      return parentModuleBuilder;
    }

    public JHipsterModuleMavenPlugins build() {
      return new JHipsterModuleMavenPlugins(this);
    }
  }
}
