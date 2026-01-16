# Spring Web template to avoid boilerplate code

This project only provides dependencies spring-web related, some logs, actuator for metrics and lombok.
The main responsibility here is to try and keep dependencies to latest versions

Also, containers were added since in today's world almost everything lives in containers.
So, a Dockerfile with security and following docker best practises, for java applications, was added. Docker-compose as
well, to make executions easier (especially in branches with more than 1 container, like dbs, mocks, etc.)

## Usage

Copy the project and change `application_name` in configuration and packages

## List of branches with extra spring features

* functional-controller -> functional controller using features like reading path param, request body, validation on
  input data
* basic-auth -> on top of functional controller, spring security with basic auth was added. different roles and
  different endpoints to mix it up
* spring-data-h2 -> service that uses spring-data-jpa with h2 for local executions and postgres for docker-compose
* spring-data-rest -> used spring-data-rest to create different APIs based on repository (db-centric architecture)
