Feature: project management

  Background:
    Given I apply "init" module to default project
      | projectName | Test project |

  Scenario: Should download project
    When I download the created project
    Then I should have "test-project.zip" project

  Scenario: Should format project
    When I format the created project
    Then I should have formatted project

  Scenario: Should get project information
    When I get the created project information
    Then I should have modules
      | Slug |
      | init |
    And I should have properties
      | projectName | Test project |

  Scenario: Should get presets definition
    When I get the presets definition
    Then I should have preset names
      | test preset one |
      | test preset two |
    And I should have preset modules
      | test-module-one   |
      | test-module-two   |
      | test-module-three |
      | test-module-four  |
