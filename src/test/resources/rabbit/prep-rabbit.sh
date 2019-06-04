#!/usr/bin/env bash

rabbitmqadmin declare queue name=plantlive durable=true

rabbitmqctl add_user plantlive_rabbituser plantlive_nevertell
rabbitmqctl set_permissions -p / plantlive_rabbituser "" "" ".*"
rabbitmqctl set_user_tags plantlive_rabbituser test-consumer