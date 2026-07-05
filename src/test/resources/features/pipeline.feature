Feature: Verify end-to-end message delivery pipeline

  @SmokeTest
  Scenario: Sent message is successfully saved as the latest record
    When I send a message "Testing latest record!" to the queue
    Then the latest record in the database should have text "Testing latest record!"