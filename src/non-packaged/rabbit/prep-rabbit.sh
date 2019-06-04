#!/usr/bin/env bash

rabbitmqctl add_user plantlive_rabbituser plantlive_nevertell
# Configure, write, read
rabbitmqctl set_permissions -p / plantlive_rabbituser "" "" ".*"
rabbitmqctl set_user_tags plantlive_rabbituser test-consumer

rabbitmqadmin declare queue name=plantlive durable=true

# TODO Just for testing
rabbitmq-diagnostics listeners