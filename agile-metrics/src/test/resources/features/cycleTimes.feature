Feature: Cycle Times management

  @CleanDatabase
  Scenario: Add a cycle time to a given team
    Given a team with name "Squad A"
    And the client send a POST with previous team
    And a cycle time with start date "2020-02-10" and end date "2020-02-13"
    When the client send a POST with previous cycle time and previous created team
    Then the client receives status code of 201
    And the client receives a cycle time from "2020-02-10" to "2020-02-13" and non-empty id
