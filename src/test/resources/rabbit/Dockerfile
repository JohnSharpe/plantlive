FROM rabbitmq:3.7.15-management

RUN rabbitmq-plugins enable --offline rabbitmq_mqtt

COPY prep-rabbit.sh /root/
RUN chmod 700 /root/prep-rabbit.sh