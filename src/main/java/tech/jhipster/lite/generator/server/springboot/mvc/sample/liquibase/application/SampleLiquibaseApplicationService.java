package tech.jhipster.lite.generator.server.springboot.mvc.sample.liquibase.application;

import org.springframework.stereotype.Service;
import tech.jhipster.lite.generator.server.springboot.mvc.sample.liquibase.domain.SampleLiquibaseModuleFactory;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@Service
public class SampleLiquibaseApplicationService {

  private final SampleLiquibaseModuleFactory factory;

  public SampleLiquibaseApplicationService() {
    factory = new SampleLiquibaseModuleFactory();
  }

  public JHipsterModule buildModule(JHipsterModuleProperties properties) {
    return factory.buildModule(properties);
  }
}
