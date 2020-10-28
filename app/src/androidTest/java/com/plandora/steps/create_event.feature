Feature: Create Event
  The user is able to create a new event. Therefore he is asked to specify the date, a
  title, the category and whether the event is annual or one-time.

  Scenario: event successfully created
    Given the user is on CreateEventActivity
    When the user enters valid data
    And the user clicks submit
    Then a new event is created
    Then the user lands on Dashboard

  Scenario: failed to create event
    Given the user is on CreateEventActivity
    When the user enters invalid data
    And the user clicks submit
    Then the user gets error message

  Scenario: event creation is aborted
    Given the user is on CreateEventActivity
    When the user clicks back button
    Then the user lands on Dashboard
    Then the data already entered is stored as draft