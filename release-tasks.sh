#!/usr/bin/env bash

# Generate a configuration file with environment variables
envsubst < "config-template.yml" > "config.yml"

# Migrate the database
java -jar target/plantlive-1.0-SNAPSHOT.jar db migrate config.yml