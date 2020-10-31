Feature: Filter Upcoming Events
  The user is able to filter the dashboard by specific events

  Scenario: apply text search
    Given the user clicked on search bar
    When the user enters text in search bar
    Then the dashboard is filtered by matching text

  Scenario: apply search for event types
    Given the user clicked on search bar
    When the user clicks on type label
    Then the dashboard is filtered by given type

  Scenario: search is aborted
    Given the user clicked on search bar
    When the user clicks on back button
    Then the search bar disappears