package tech.jhipster.lite.generator.server.documentation.jmolecules.application;

import org.springframework.stereotype.Service;
import tech.jhipster.lite.generator.server.documentation.jmolecules.domain.JMoleculesModuleFactory;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@Service
public class JMoleculesApplicationService {

  private final JMoleculesModuleFactory factory;

  public JMoleculesApplicationService() {
    factory = new JMoleculesModuleFactory();
  }

  public JHipsterModule buildModule(JHipsterModuleProperties properties) {
    return factory.buildModule(properties);
  }
}
