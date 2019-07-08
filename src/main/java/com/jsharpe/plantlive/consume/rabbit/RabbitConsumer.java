package com.jsharpe.plantlive.consume.rabbit;

import com.jsharpe.plantlive.consume.InService;
import com.jsharpe.plantlive.exceptions.ConsumeException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class RabbitConsumer implements Managed {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConsumer.class);
    private static final boolean AUTO_ACK = true;

    private final InService inService;
    private final ConnectionFactory connectionFactory;
    private final String queue;

    private Connection connection;
    private Channel channel;

    public RabbitConsumer(
            final InService inService,
            final ConnectionFactory connectionFactory,
            final String queue
    ) {
        this.inService = inService;
        this.connectionFactory = connectionFactory;
        this.queue = queue;
    }

    @Override
    public void start() throws Exception {

        // Connections are meant to be long-lived and opening them is expensive.
        this.connection = this.connectionFactory.newConnection();
        LOGGER.debug("Opened a Rabbit connection.");
        // Channels are also meant to be long-lived but some recoverable protocols might cause them to close.
        this.channel = this.connection.createChannel();
        LOGGER.debug("Opened a Rabbit channel.");

        // Use the default, direct exchange.
        // Passively declare the queue - we'll sort it out externally
        this.channel.queueDeclarePassive(this.queue);
        LOGGER.info("Queue [{}] exists, starting consumer...", this.queue);

        this.channel.basicConsume(
                this.queue,
                AUTO_ACK,
                ((consumerTag, delivery) -> {

                    final String message = new String(delivery.getBody(), StandardCharsets.UTF_8.name());
                    LOGGER.info("Received a message with consumerTag: {}, deliveryTag: {}, content: {}",
                            consumerTag, delivery.getEnvelope().getDeliveryTag(), message);

                    // Split on semi-colon
                    final String[] messageParts = message.split(";");

                    if (messageParts.length != 6) {
                        LOGGER.warn("Cannot consume message! {}", message);
                        return;
                    }

                    try {
                        // TODO Lots of assumptions here, can we not get Rabbit to take care of this?
                        // TODO Handle NumberFormatException more elegantly
                        this.inService.write(
                                Long.valueOf(messageParts[0]),
                                messageParts[1],
                                delivery.getProperties().getTimestamp(),
                                Double.valueOf(messageParts[2]),
                                Double.valueOf(messageParts[3]),
                                Double.valueOf(messageParts[4]),
                                Double.valueOf(messageParts[5])
                        );
                    } catch (ConsumeException e) {
                        LOGGER.warn("Cannot consume message!", e);
                    }
                }),
                consumerTag -> LOGGER.warn("Callback cancelled! {}", consumerTag)
        );
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Closing Rabbit channel and connection.");
        this.channel.close();
        this.connection.close();
    }

}
