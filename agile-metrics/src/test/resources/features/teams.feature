Feature: Team management

  @CleanDatabase
  Scenario: Add a team with basic info
    Given a team with name "Squad A"
    When the client send a POST with previous team
    Then the client receives status code of 201
    And the client receives a team with name "Squad A" and non-empty id

  @CleanDatabase
  Scenario: Add three teams and retrieve them
    And a team with name "Squad A"
    And the client send a POST with previous team
    And a team with name "Squad B"
    And the client send a POST with previous team
    And a team with name "Squad C"
    And the client send a POST with previous team
    When the client retrieves all teams
    Then the client receives status code of 200
    And the client receives 3 teams

