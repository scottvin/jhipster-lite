package tech.jhipster.lite.project.infrastructure.secondary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(NativeHints.class)
class ProjectFormatterConfiguration {

  private static final Logger log = LoggerFactory.getLogger(ProjectFormatterConfiguration.class);

  @Bean
  public ProjectFormatter projectFormatter(NpmInstallationReader npmInstallation) {
    return switch (npmInstallation.get()) {
      case UNIX -> unixFormatter();
      case WINDOWS -> windowsFormatter();
      case NONE -> traceFormatter();
    };
  }

  private NpmProjectFormatter unixFormatter() {
    log.info("Using unix formatter");

    return new NpmProjectFormatter("npm");
  }

  private NpmProjectFormatter windowsFormatter() {
    log.info("Using windows formatter");

    return new NpmProjectFormatter("npm.cmd");
  }

  private TraceProjectFormatter traceFormatter() {
    log.info("Using trace formatter");

    return new TraceProjectFormatter();
  }
}
