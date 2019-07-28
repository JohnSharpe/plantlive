package com.jsharpe.plantlive.consume.rabbit;

import com.jsharpe.plantlive.consume.InService;
import com.jsharpe.plantlive.exceptions.ConsumeException;
import com.rabbitmq.client.*;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

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
                new ConsumeCallback(this.inService),
                consumerTag -> LOGGER.warn("Callback cancelled! {}", consumerTag)
        );

    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Closing Rabbit channel and connection.");
        this.channel.close();
        this.connection.close();
    }

    public static class ConsumeCallback implements DeliverCallback {

        private final InService inService;

        public ConsumeCallback(final InService inService) {
            this.inService = inService;
        }

        @Override
        public void handle(String consumerTag, Delivery delivery) throws IOException {

            final String message = new String(delivery.getBody(), StandardCharsets.UTF_8.name());
            LOGGER.info("Received a message with consumerTag: {}, deliveryTag: {}, content: {}",
                    consumerTag, delivery.getEnvelope().getDeliveryTag(), message);

            // Split on semi-colon
            // user_id;type;password;temperature;humidity;light;conductivity
            final String[] messageParts = message.split(";");

            if (messageParts.length != 7) {
                LOGGER.warn("Cannot consume message! {}", message);
                return;
            }

            final UUID userId;
            try {
                userId = UUID.fromString(messageParts[0]);
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Cannot extract a userId from message! {}", messageParts[0]);
                return;
            }

            final double temperature;
            try {
                temperature = Double.valueOf(messageParts[3]);
            } catch (NumberFormatException e) {
                LOGGER.warn("Cannot extract a temperature from message! {}", messageParts[2]);
                return;
            }

            final double humidity;
            try {
                humidity = Double.valueOf(messageParts[4]);
            } catch (NumberFormatException e) {
                LOGGER.warn("Cannot extract a humidity from message! {}", messageParts[3]);
                return;
            }

            final double light;
            try {
                light = Double.valueOf(messageParts[5]);
            } catch (NumberFormatException e) {
                LOGGER.warn("Cannot extract a light from message! {}", messageParts[4]);
                return;
            }

            final double conductivity;
            try {
                conductivity = Double.valueOf(messageParts[6]);
            } catch (NumberFormatException e) {
                LOGGER.warn("Cannot extract a conductivity from message! {}", messageParts[5]);
                return;
            }

            try {
                this.inService.write(
                        userId,
                        messageParts[1],
                        messageParts[2],
                        delivery.getProperties().getTimestamp(),
                        temperature,
                        humidity,
                        light,
                        conductivity
                );
            } catch (ConsumeException e) {
                LOGGER.warn("Cannot consume message!", e);
            }
        }

    }

}
