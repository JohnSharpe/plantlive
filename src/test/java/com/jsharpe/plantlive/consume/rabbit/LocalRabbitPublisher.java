package com.jsharpe.plantlive.consume.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class LocalRabbitPublisher {

    private final String queue;
    private Channel channel;

    public LocalRabbitPublisher(
            final String host,
            final Integer port,
            final String username,
            final String password,
            final String vhost,
            final String queue
    ) throws IOException, TimeoutException {

        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        if (StringUtils.isNotBlank(vhost)) {
            connectionFactory.setVirtualHost(vhost);
        }

        // TODO Implement this!
        // connectionFactory.useSslProtocol();

        final Connection connection = connectionFactory.newConnection();
        this.channel = connection.createChannel();
        this.queue = queue;

    }

    public void publish(final String message) throws IOException {
        this.channel.basicPublish(
                "",
                this.queue,
                true,
                false,
                null,
                message.getBytes(StandardCharsets.UTF_8)
           );
    }

}
