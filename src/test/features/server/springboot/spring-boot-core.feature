Feature: Spring boot core

  Scenario: Should apply spring boot core module
    When I apply "spring-boot" module to default project with maven file
      | packageName | tech.jhipster.chips |
      | baseName    | jhipster            |
    Then I should have files in "src/main/java/tech/jhipster/chips"
      | JhipsterApp.java |

  Scenario: Should handle application configuration with properties format
    When I apply "spring-boot" module to default project with maven file
      | packageName               | tech.jhipster.chips |
      | baseName                  | jhipster            |
      | springConfigurationFormat | properties          |
    Then I should have files in "src/main/resources/config/"
      | application.properties |
    Then I should have files in "src/test/resources/config/"
      | application-test.properties |
    Then I should not have files in "src/main/resources/config/"
      | application.yml |
    Then I should not have files in "src/test/resources/config/"
      | application-test.yml |

  Scenario: Should handle application configuration with YAML format
    When I apply "spring-boot" module to default project with maven file
      | packageName               | tech.jhipster.chips |
      | baseName                  | jhipster            |
      | springConfigurationFormat | yaml                |
    Then I should have files in "src/main/resources/config/"
      | application.yml |
    Then I should have files in "src/test/resources/config/"
      | application-test.yml |
    Then I should not have files in "src/main/resources/config/"
      | application.properties |
    Then I should not have files in "src/test/resources/config/"
      | application-test.properties |
