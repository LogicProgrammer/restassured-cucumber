@go-rest @api
Feature: go-rest api feature

  As a user, I'm able to access go-rest api

  @get-all
  Scenario: user able to all users
    Given user provides endPoint as "/public-api/users".
    And user sets the method as "GET".
    And user hits the service.
    And response status code will be "200".


  @get-examples
  Scenario Outline: user able to access selected user details via api
    Given user provides endPoint as "/public-api/users/<id>".
    And user sets the method as "GET".
    And user hits the service.
    And response status code will be "200".

    Examples: user id
      | id |
      | 1  |

  @get-user
  Scenario: user able to access user detail via api
    Given user provides endPoint as "/public-api/users/1".
    And user sets the method as "GET".
    And user hits the service.
    And response status code will be "200".
    And response contains following values :
      | meta        | null                |
      | data.name   | Shwet Arora         |
      | data.email  | shwet_arora@beer.io |
      | data.gender | Female              |

  @create-user
  Scenario: user able to create
    Given user provides endPoint as "/public-api/users".
    And user sets the method as "POST".
    And user hits the service with request body:
      """
      {
        "name": "Tenali Ramakrishna10",
        "gender": "Male",
        "email": "tenali.ramakrishna@15ce10.com",
        "status": "Active"
      }
      """
    And response status code will be "200".
    And response body is according to the schema file "./src/test/resources/schema/create-user.json"


  @delete-user
  Scenario: delete user
    Given user provides endPoint as "/public-api/users/1480".
    And user sets the method as "DELETE".
    And user hits the service.
    And response status code will be "200".
    And response contains "code" as 204.

  @add-delete
  Scenario: sample scenario
    Given user creates a new user.
    And response status code will be "200".
    And response body is according to the schema file "./src/test/resources/schema/create-user.json"
    And user stores new user id.
    And user deletes newly created user.
    And response status code will be "200".
    And response contains "code" as 204.
    And user fetches user details of newly created.
    And response status code will be "200".
    And response contains "code" as 404.
    And response contains following values :
      | data.message | Resource not found |