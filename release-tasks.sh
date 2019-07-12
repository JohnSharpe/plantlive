#!/usr/bin/env bash

PORT=${PORT}
DATABASE_URL=${DATABASE_URL}

# Fail if any of these environment variables are unset
[[ -z "$PORT" ]] && echo 'PORT must be set.' && exit 1;
[[ -z "$DATABASE_URL" ]] && echo 'DATABASE_URL must be set.' && exit 1;

# jdbc:postgresql:// + (Replace everything before & including the @ with blank)
DATABASE_JDBC_URL="jdbc:postgresql://$(echo ${DATABASE_URL} | sed 's/.*@//' )"

# Replace everything before & including the :// with blank
# Then replace everything after + including a colon
DATABASE_USER=$(echo ${DATABASE_URL} | sed 's/.*:\/\///' | sed 's/:.*//' )

# Replace everything before & including the :// with blank
# Then replace everything after & including the @ with blank
# Then replace everything before & including a colon with blank
DATABASE_PASS=$(echo ${DATABASE_URL} | sed 's/.*:\/\///' | sed 's/@.*//' | sed 's/.*://' )

# Generate a configuration file with environment variables
printf "
server:
  applicationConnectors:
    - type: http
      port: $PORT
  adminConnectors:
    - type: http
      port: 4000

persistence:
  type: sql
  database:
    # the name of your JDBC driver
    driverClass: org.postgresql.Driver
    # the username/password
    user: $DATABASE_USER
    password: $DATABASE_PASS

    # the JDBC URL
    url: $DATABASE_JDBC_URL

    # any properties specific to your JDBC driver:
    properties:
      charSet: UTF-8

    # the maximum amount of time to wait on an empty pool before throwing an exception
    maxWaitForConnection: 1s

    # the SQL query to run when validating a connection's liveness
    validationQuery: '/* MyService Health Check */ SELECT 1'

    # the timeout before a connection validation queries fail
    validationQueryTimeout: 3s

    # the minimum, maximum number of connections to keep open
    minSize: 8
    maxSize: 32

    # whether or not idle connections should be validated
    checkConnectionWhileIdle: false

    # the amount of time to sleep between runs of the idle connection validation, abandoned cleaner and idle pool resizing
    evictionInterval: 10s

    # the minimum amount of time an connection must sit idle in the pool before it is eligible for eviction
    minIdleTime: 1 minute

in:
  type: nop

#in:
  #type: rabbit
  #host: 127.0.0.1
  #port: 5672
  #username: plantlive_rabbituser
  #password: plantlive_nevertell
  #queue: plantlive

out:
  type: http
" > target/config.yml