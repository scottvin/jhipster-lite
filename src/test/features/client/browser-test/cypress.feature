Feature: Cypress module

  Scenario: Should apply cypress-component-tests module
    When I apply "cypress-component-tests" module to default project with package json
      | serverPort | 9000 |
    Then I should have files in "src/test/webapp/component"
      | .eslintrc.cjs |

  Scenario: Should apply cypress-e2e module
    When I apply "cypress-e2e" module to default project with package json
      | serverPort | 9000 |
    Then I should have files in "src/test/webapp/e2e"
      | .eslintrc.cjs |
