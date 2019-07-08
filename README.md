# plantlive [![Build Status](https://travis-ci.com/JohnSharpe/plantlive.svg?branch=master)](https://travis-ci.com/JohnSharpe/plantlive)
A Dropwizard application to read sensor data from a queue, save to a database and serve up a summary.
Currently implemented as a RabbitMQ consumer, connection to an SQL database and some HTTP endpoints.

## Configuration
See the yml files in `src/test/resources` for examples.

## Software Requirements
- java 1.8
- maven 3
- docker (integration + load testing)

Implementation specifics:
- rabbit 3.7.15
- postgres 9.6

## Build and Test
Maven is used to build the project.  
`mvn clean verify`

## Run
If you're using an SQL database, it's up to you to create the user and (blank) database according to your configuration.  
From there, the output can migrate with Liquibase to get your tables into shape:  
`java -jar target/xyz.jar db status your-config.yml`  
`java -jar target/xyz.jar db migrate your-config.yml`

This will generate a normal Java jar.  
`java -jar target/xyz.jar server your-config.yml`

## Api
The application has the following resources:  
When `Accept=text/html` 
- GET "/" - Gives you a simple welcome page including a form to get you to the next endpoint.
- GET "/?id=123" - Gives you a summary of the plant with supplied id over the past 24 hours.

When `Accept=application/json`
- GET "/?id=123" - Gives you a JSON representation of the summary for plant with the supplied id.