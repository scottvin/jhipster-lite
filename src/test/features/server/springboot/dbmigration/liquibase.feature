Feature: Liquibase module

  Scenario: Should apply liquibase module
    When I apply "liquibase" module to default project with maven file
      | packageName | tech.jhipster.chips |
    Then I should have files in "src/main/resources/config/liquibase"
      | master.xml |

  Scenario: Should apply liquibase-async module
    When I apply "liquibase-async" module to default project with maven file
      | packageName | tech.jhipster.chips |
    Then I should have files in "src/main/java/tech/jhipster/chips/wire/liquibase/infrastructure/secondary"
      | AsyncSpringLiquibase.java |
