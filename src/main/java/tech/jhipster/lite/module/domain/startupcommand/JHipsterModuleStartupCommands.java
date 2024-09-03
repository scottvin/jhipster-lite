package tech.jhipster.lite.module.domain.startupcommand;

import java.util.ArrayList;
import java.util.Collection;
import tech.jhipster.lite.module.domain.JHipsterModule.JHipsterModuleBuilder;
import tech.jhipster.lite.shared.error.domain.Assert;

public final class JHipsterModuleStartupCommands {

  private final JHipsterStartupCommands commands;

  private JHipsterModuleStartupCommands(JHipsterModuleStartupCommandsBuilder builder) {
    commands = new JHipsterStartupCommands(builder.commands);
  }

  public static JHipsterModuleStartupCommandsBuilder builder(JHipsterModuleBuilder module) {
    return new JHipsterModuleStartupCommandsBuilder(module);
  }

  public JHipsterStartupCommands commands() {
    return commands;
  }

  public static final class JHipsterModuleStartupCommandsBuilder {

    private final JHipsterModuleBuilder module;
    private final Collection<JHipsterStartupCommand> commands = new ArrayList<>();

    private JHipsterModuleStartupCommandsBuilder(JHipsterModuleBuilder module) {
      Assert.notNull("module", module);

      this.module = module;
    }

    public JHipsterModuleStartupCommandsBuilder dockerCompose(DockerComposeFile dockerComposeFile) {
      commands.add(new DockerComposeStartupCommandLine(dockerComposeFile));
      return this;
    }

    public JHipsterModuleStartupCommandsBuilder dockerCompose(String dockerComposeFile) {
      return dockerCompose(new DockerComposeFile(dockerComposeFile));
    }

    public JHipsterModuleStartupCommandsBuilder maven(String commandLineParameters) {
      commands.add(new MavenStartupCommandLine(commandLineParameters));
      return this;
    }

    public JHipsterModuleStartupCommandsBuilder gradle(String commandLineParameters) {
      commands.add(new GradleStartupCommandLine(commandLineParameters));
      return this;
    }

    public JHipsterModuleBuilder and() {
      return module;
    }

    public JHipsterModuleStartupCommands build() {
      return new JHipsterModuleStartupCommands(this);
    }
  }
}
