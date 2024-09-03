package tech.jhipster.jhlitest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import tech.jhipster.jhlitest.common.domain.Generated;

@SpringBootApplication
@ExcludeFromGeneratedCodeCoverage(reason = "Not testing logs")
public class MyappApp {

  private static final Logger log = LoggerFactory.getLogger(MyappApp.class);

  public static void main(String[] args) {
    Environment env = SpringApplication.run(MyappApp.class, args).getEnvironment();

    if (log.isInfoEnabled()) {
      log.info(ApplicationStartupTraces.of(env));
    }
  }
}
