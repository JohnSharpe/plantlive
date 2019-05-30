package com.jsharpe.plantlive.consume.rabbit;

import com.jsharpe.plantlive.consume.InService;
import com.jsharpe.plantlive.exceptions.ConsumeException;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.dropwizard.lifecycle.Managed;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

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
            final String host,
            final Integer port,
            final String username,
            final String password,
            final String vhost,
            final String queue
    ) throws KeyManagementException, NoSuchAlgorithmException {
        this.inService = inService;

        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        if (StringUtils.isNotBlank(vhost)) {
            connectionFactory.setVirtualHost(vhost);
        }

        // This is non-negotiable
        connectionFactory.useSslProtocol();

        this.connectionFactory = connectionFactory;

        this.queue = queue;
    }

    @Override
    public void start() throws Exception {

        // Connections are meant to be long-lived and opening them is expensive.
        this.connection = this.connectionFactory.newConnection();
        // Channels are also meant to be long-lived but some recoverable protocols might cause them to close.
        this.channel = this.connection.createChannel();

        // Use the default, direct exchange
        // Note this is for exclusive use - if this application was clustered this would need to be revisited.
        this.channel.queueDeclare(this.queue, true, true, true, null);

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
                        this.inService.write(
                                Long.valueOf(messageParts[0]),
                                messageParts[1],
                                delivery.getProperties().getTimestamp(),
                                Integer.valueOf(messageParts[2]),
                                Integer.valueOf(messageParts[3]),
                                Integer.valueOf(messageParts[4]),
                                Integer.valueOf(messageParts[5])
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
        this.channel.close();
        this.connection.close();
    }

}
