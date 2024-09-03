Feature: Frontend server module

  Scenario: Should apply frontend server maven module
    When I apply "frontend-maven-plugin" module to default project with maven file
      | packageName | tech.jhipster.chips |
      | baseName    | jhipster            |
    Then I should have files in "src/main/java/tech/jhipster/chips/wire/frontend/infrastructure/primary"
      | RedirectionResource.java |

  Scenario: Should apply frontend server gradle module
    When I apply "node-gradle-plugin" module to default project with gradle build
      | packageName | tech.jhipster.chips |
      | baseName    | jhipster            |
    Then I should have files in "src/main/java/tech/jhipster/chips/wire/frontend/infrastructure/primary"
      | RedirectionResource.java |
