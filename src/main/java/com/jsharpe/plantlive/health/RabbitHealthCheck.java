package com.jsharpe.plantlive.health;

import com.codahale.metrics.health.HealthCheck;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * TODO Test this!
 */
public class RabbitHealthCheck extends HealthCheck {

    private final ConnectionFactory connectionFactory;
    private final String queue;

    public RabbitHealthCheck(
            final ConnectionFactory connectionFactory,
            final String queue
    ) {
        this.connectionFactory = connectionFactory;
        this.queue = queue;
    }

    @Override
    protected Result check() throws Exception {

        // TODO Holding open a connection is ok, right?

        try (final Connection connection = this.connectionFactory.newConnection()) {
            if (connection == null || !connection.isOpen()) {
                return Result.unhealthy("No Rabbit connection available");
            }

            final Channel channel = connection.createChannel();

            if (channel == null || !channel.isOpen()) {
                return Result.unhealthy("Rabbit channel is not open");
            }

            try {
                final AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(this.queue);

                if (declareOk.getConsumerCount() > 0) {
                    return Result.healthy();
                } else {
                    return Result.unhealthy("No consumers attached to " + this.queue);
                }

            } catch (IOException e) {
                return Result.unhealthy(e);
            }
        }

    }
}
