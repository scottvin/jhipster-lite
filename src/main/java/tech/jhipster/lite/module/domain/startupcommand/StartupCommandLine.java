package tech.jhipster.lite.module.domain.startupcommand;

import tech.jhipster.lite.shared.error.domain.Assert;

public record StartupCommandLine(String commandLine) {
  public StartupCommandLine {
    Assert.notBlank("commandLine", commandLine);
  }

  public String get() {
    return commandLine();
  }
}
