package com.jsharpe.plantlive.health;

import com.codahale.metrics.health.HealthCheck;
import com.jsharpe.plantlive.UnitTest;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.AMQImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.io.IOException;

@Category(UnitTest.class)
public class RabbitHealthCheckTest {

    @Test
    public void testNoConnectionAvailable() throws Exception {
        // Given
        final ConnectionFactory connectionFactory = Mockito.mock(ConnectionFactory.class);
        Mockito.when(connectionFactory.newConnection()).thenReturn(null);
        final String queue = "queue";

        final RabbitHealthCheck rabbitHealthCheck = new RabbitHealthCheck(connectionFactory, queue);

        // When
        final HealthCheck.Result result = rabbitHealthCheck.check();

        // Then
        Assert.assertFalse(result.isHealthy());
    }

    @Test
    public void testNoOpenConnectionAvailable() throws Exception {
        // Given
        final Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.isOpen()).thenReturn(false);

        final ConnectionFactory connectionFactory = Mockito.mock(ConnectionFactory.class);

        Mockito.when(connectionFactory.newConnection()).thenReturn(connection);
        final String queue = "queue";

        final RabbitHealthCheck rabbitHealthCheck = new RabbitHealthCheck(connectionFactory, queue);

        // When
        final HealthCheck.Result result = rabbitHealthCheck.check();

        // Then
        Assert.assertFalse(result.isHealthy());
    }

    @Test
    public void testNoChannelAvailable() throws Exception {
        // Given
        final Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.isOpen()).thenReturn(true);
        Mockito.when(connection.createChannel()).thenReturn(null);

        final ConnectionFactory connectionFactory = Mockito.mock(ConnectionFactory.class);

        Mockito.when(connectionFactory.newConnection()).thenReturn(connection);
        final String queue = "queue";

        final RabbitHealthCheck rabbitHealthCheck = new RabbitHealthCheck(connectionFactory, queue);

        // When
        final HealthCheck.Result result = rabbitHealthCheck.check();

        // Then
        Assert.assertFalse(result.isHealthy());
    }

    @Test
    public void testNoOpenChannelAvailable() throws Exception {
        // Given
        final Channel channel = Mockito.mock(Channel.class);
        Mockito.when(channel.isOpen()).thenReturn(false);

        final Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.isOpen()).thenReturn(true);
        Mockito.when(connection.createChannel()).thenReturn(channel);

        final ConnectionFactory connectionFactory = Mockito.mock(ConnectionFactory.class);

        Mockito.when(connectionFactory.newConnection()).thenReturn(connection);
        final String queue = "queue";

        final RabbitHealthCheck rabbitHealthCheck = new RabbitHealthCheck(connectionFactory, queue);

        // When
        final HealthCheck.Result result = rabbitHealthCheck.check();

        // Then
        Assert.assertFalse(result.isHealthy());
    }

    @Test
    public void testNoQueue() throws Exception {
        // Given
        final String queue = "queue";

        final Channel channel = Mockito.mock(Channel.class);
        Mockito.when(channel.isOpen()).thenReturn(true);

        //noinspection RedundantArrayCreation,unchecked
        Mockito.when(channel.queueDeclarePassive(queue)).thenThrow(
                new Class[]{IOException.class}
        );

        final Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.isOpen()).thenReturn(true);
        Mockito.when(connection.createChannel()).thenReturn(channel);

        final ConnectionFactory connectionFactory = Mockito.mock(ConnectionFactory.class);

        Mockito.when(connectionFactory.newConnection()).thenReturn(connection);

        final RabbitHealthCheck rabbitHealthCheck = new RabbitHealthCheck(connectionFactory, queue);

        // When
        final HealthCheck.Result result = rabbitHealthCheck.check();

        // Then
        Assert.assertFalse(result.isHealthy());
    }

    @Test
    public void testNoConsumers() throws Exception {
        // Given
        final String queue = "queue";

        final Channel channel = Mockito.mock(Channel.class);
        Mockito.when(channel.isOpen()).thenReturn(true);
        Mockito.when(channel.queueDeclarePassive(queue)).thenReturn(new AMQImpl.Queue.DeclareOk(queue, 0, 0));

        final Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.isOpen()).thenReturn(true);
        Mockito.when(connection.createChannel()).thenReturn(channel);

        final ConnectionFactory connectionFactory = Mockito.mock(ConnectionFactory.class);

        Mockito.when(connectionFactory.newConnection()).thenReturn(connection);

        final RabbitHealthCheck rabbitHealthCheck = new RabbitHealthCheck(connectionFactory, queue);

        // When
        final HealthCheck.Result result = rabbitHealthCheck.check();

        // Then
        Assert.assertFalse(result.isHealthy());
    }

    @Test
    public void testWeGotThereInTheEnd() throws Exception {
        // Given
        final String queue = "queue";

        final Channel channel = Mockito.mock(Channel.class);
        Mockito.when(channel.isOpen()).thenReturn(true);
        Mockito.when(channel.queueDeclarePassive(queue)).thenReturn(new AMQImpl.Queue.DeclareOk(queue, 0, 1));

        final Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.isOpen()).thenReturn(true);
        Mockito.when(connection.createChannel()).thenReturn(channel);

        final ConnectionFactory connectionFactory = Mockito.mock(ConnectionFactory.class);

        Mockito.when(connectionFactory.newConnection()).thenReturn(connection);

        final RabbitHealthCheck rabbitHealthCheck = new RabbitHealthCheck(connectionFactory, queue);

        // When
        final HealthCheck.Result result = rabbitHealthCheck.check();

        // Then
        Assert.assertTrue(result.isHealthy());
    }

}
