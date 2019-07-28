package com.jsharpe.plantlive.consume.rabbit;

import com.jsharpe.plantlive.UnitTest;
import com.jsharpe.plantlive.consume.InService;
import com.jsharpe.plantlive.exceptions.ConsumeException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.Envelope;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Category(UnitTest.class)
public class RabbitConsumeTest {

    @Test
    public void testBadUUIDConsume() throws IOException, ConsumeException {
        // Given
        final InService inService = Mockito.mock(InService.class);
        final RabbitConsumer.ConsumeCallback consumeCallback = new RabbitConsumer.ConsumeCallback(inService);
        final Envelope envelope = new Envelope(1, false, "plantlive-exchange", "plantlive");
        final AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();

        final String candidate = "1;rose;pA55w0rD;18;87;44;21";
        final byte[] body = StandardCharsets.UTF_8.encode(candidate).array();

        // When
        consumeCallback.handle("someTag", new Delivery(envelope, basicProperties, body));

        // Then
        Mockito.verify(inService, Mockito.never()).write(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble()
        );
    }

    @Test
    public void testImpossiblySmallMessage() throws IOException, ConsumeException {
        // Given
        final InService inService = Mockito.mock(InService.class);
        final RabbitConsumer.ConsumeCallback consumeCallback = new RabbitConsumer.ConsumeCallback(inService);
        final Envelope envelope = new Envelope(1, false, "plantlive-exchange", "plantlive");
        final AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();

        final String candidate = UUID.randomUUID().toString() + ";pA55w0rD;18;87;44;21";
        final byte[] body = StandardCharsets.UTF_8.encode(candidate).array();

        // When
        consumeCallback.handle("someTag", new Delivery(envelope, basicProperties, body));

        // Then
        Mockito.verify(inService, Mockito.never()).write(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble()
        );
    }

    @Test
    public void testImpossiblyLargeMessage() throws IOException, ConsumeException {
        // Given
        final InService inService = Mockito.mock(InService.class);
        final RabbitConsumer.ConsumeCallback consumeCallback = new RabbitConsumer.ConsumeCallback(inService);
        final Envelope envelope = new Envelope(1, false, "plantlive-exchange", "plantlive");
        final AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();

        final String candidate = UUID.randomUUID().toString() + ";rose;pA55w0rD;18;87;44;21;oops";
        final byte[] body = StandardCharsets.UTF_8.encode(candidate).array();

        // When
        consumeCallback.handle("someTag", new Delivery(envelope, basicProperties, body));

        // Then
        Mockito.verify(inService, Mockito.never()).write(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble()
        );
    }

    @Test
    public void testBadTemperature() throws IOException, ConsumeException {
        // Given
        final InService inService = Mockito.mock(InService.class);
        final RabbitConsumer.ConsumeCallback consumeCallback = new RabbitConsumer.ConsumeCallback(inService);
        final Envelope envelope = new Envelope(1, false, "plantlive-exchange", "plantlive");
        final AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();

        final String candidate = UUID.randomUUID().toString() + ";rose;pA55w0rD;NO;87;44;21";
        final byte[] body = StandardCharsets.UTF_8.encode(candidate).array();

        // When
        consumeCallback.handle("someTag", new Delivery(envelope, basicProperties, body));

        // Then
        Mockito.verify(inService, Mockito.never()).write(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble()
        );
    }

    @Test
    public void testBadHumidity() throws IOException, ConsumeException {
        // Given
        final InService inService = Mockito.mock(InService.class);
        final RabbitConsumer.ConsumeCallback consumeCallback = new RabbitConsumer.ConsumeCallback(inService);
        final Envelope envelope = new Envelope(1, false, "plantlive-exchange", "plantlive");
        final AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();

        final String candidate = UUID.randomUUID().toString() + ";rose;pA55w0rD;32;NO;44;21";
        final byte[] body = StandardCharsets.UTF_8.encode(candidate).array();

        // When
        consumeCallback.handle("someTag", new Delivery(envelope, basicProperties, body));

        // Then
        Mockito.verify(inService, Mockito.never()).write(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble()
        );
    }

    @Test
    public void testBadLight() throws IOException, ConsumeException {
        // Given
        final InService inService = Mockito.mock(InService.class);
        final RabbitConsumer.ConsumeCallback consumeCallback = new RabbitConsumer.ConsumeCallback(inService);
        final Envelope envelope = new Envelope(1, false, "plantlive-exchange", "plantlive");
        final AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();

        final String candidate = UUID.randomUUID().toString() + ";rose;pA55w0rD;32;87;NO;21";
        final byte[] body = StandardCharsets.UTF_8.encode(candidate).array();

        // When
        consumeCallback.handle("someTag", new Delivery(envelope, basicProperties, body));

        // Then
        Mockito.verify(inService, Mockito.never()).write(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble()
        );
    }

    @Test
    public void testBadConductivity() throws IOException, ConsumeException {
        // Given
        final InService inService = Mockito.mock(InService.class);
        final RabbitConsumer.ConsumeCallback consumeCallback = new RabbitConsumer.ConsumeCallback(inService);
        final Envelope envelope = new Envelope(1, false, "plantlive-exchange", "plantlive");
        final AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();

        final String candidate = UUID.randomUUID().toString() + ";rose;pA55w0rD;32;87;44;No";
        final byte[] body = StandardCharsets.UTF_8.encode(candidate).array();

        // When
        consumeCallback.handle("someTag", new Delivery(envelope, basicProperties, body));

        // Then
        Mockito.verify(inService, Mockito.never()).write(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble()
        );
    }

    @Test
    public void testGoodMessage() throws IOException, ConsumeException {
        // Given
        final InService inService = Mockito.mock(InService.class);
        final RabbitConsumer.ConsumeCallback consumeCallback = new RabbitConsumer.ConsumeCallback(inService);
        final Envelope envelope = new Envelope(1, false, "plantlive-exchange", "plantlive");
        final AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();

        final String candidate = UUID.randomUUID().toString() + ";rose;pA55w0rD;32;87;44;21";
        final byte[] body = StandardCharsets.UTF_8.encode(candidate).array();

        // When
        consumeCallback.handle("someTag", new Delivery(envelope, basicProperties, body));

        // Then
        Mockito.verify(inService, Mockito.times(1)).write(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble()
        );
    }

    @Test
    public void testGoodMessageButBadImpl() throws IOException, ConsumeException {
        // Given
        final InService inService = Mockito.mock(InService.class);

        Mockito.doThrow(new ConsumeException("")).when(
                inService
        ).write(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble()
        );

        final RabbitConsumer.ConsumeCallback consumeCallback = new RabbitConsumer.ConsumeCallback(inService);
        final Envelope envelope = new Envelope(1, false, "plantlive-exchange", "plantlive");
        final AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();

        final String candidate = UUID.randomUUID().toString() + ";rose;pA55w0rD;32;87;44;21";
        final byte[] body = StandardCharsets.UTF_8.encode(candidate).array();

        // When
        consumeCallback.handle("someTag", new Delivery(envelope, basicProperties, body));

        // Then
        Mockito.verify(inService, Mockito.times(1)).write(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyDouble()
        );
    }

}
