Feature: View Event Details
  The user is able to view details of an event and edit them

  Scenario: edit event details
    Given the user is on EventDetailActivity
    When the user clicks on edit event
    Then the user lands on EditDetailActivity

  Scenario: close event details
    Given the user is on EventDetailActivity
    When the user clicks on close detailview
    Then the user lands on Dashboard



