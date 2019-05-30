# plantlive [![Build Status](https://travis-ci.com/JohnSharpe/plantlive.svg?branch=master)](https://travis-ci.com/JohnSharpe/plantlive)
A Dropwizard application to read sensor data from a queue, save to an SQL database and serve up a summary over HTTP.

## Build and Test
Maven is used to build the project.
`mvn clean verify`

This will generate a normal Java jar.
`java -jar target/xyz.jar server config.yml`
