package com.jsharpe.plantlive.consume;

import com.jsharpe.plantlive.IntegrationTest;
import com.jsharpe.plantlive.consume.rabbit.RabbitConsumer;
import com.jsharpe.plantlive.repositories.MockDetailRepository;
import com.jsharpe.plantlive.repositories.MockPlantRepository;
import com.rabbitmq.client.AuthenticationFailureException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;

@Category(IntegrationTest.class)
public class RabbitConsumerTest {

    private final MockPlantRepository plantRepository;
    private final MockDetailRepository detailRepository;
    private final InService inService;

    public RabbitConsumerTest() {
        this.plantRepository = new MockPlantRepository();
        this.detailRepository = new MockDetailRepository();
        this.inService = new InService(this.plantRepository, this.detailRepository, 24);
    }

    @Before
    public void before() {
        this.plantRepository.clear();
        this.detailRepository.clear();
    }

    @Test(expected = AuthenticationFailureException.class)
    public void testNoSuchUser() throws Exception {
        // Given
        final RabbitConsumer rabbitConsumer = new RabbitConsumer(
                this.inService,
                "127.0.0.1",
                5672,
                "some-nonexistent-user",
                "some-nonexistent-user's-password",
                null,
                "plantlivequeue"
        );

        // When
        rabbitConsumer.start();
    }

    @Test(expected = AuthenticationFailureException.class)
    public void testIncorrectPassword() throws Exception {
        // Given
        final RabbitConsumer rabbitConsumer = new RabbitConsumer(
                this.inService,
                "127.0.0.1",
                5672,
                "plantlive_rabbituser",
                "some-wrong-password",
                null,
                "plantlivequeue"
        );

        // When
        rabbitConsumer.start();
    }

    @Test(expected = IOException.class)
    public void testNoSuchQueue() throws Exception {
        // Given
        final RabbitConsumer rabbitConsumer = new RabbitConsumer(
                this.inService,
                "127.0.0.1",
                5672,
                "plantlive_rabbituser",
                "plantlive_nevertell",
                null,
                "plantlifffe"
        );

        // When
        rabbitConsumer.start();
    }

    // TEMP
    @Test
    public void testCorrectPassword() throws Exception {
        // Given
        final RabbitConsumer rabbitConsumer = new RabbitConsumer(
                this.inService,
                "127.0.0.1",
                5672,
                "plantlive_rabbituser",
                "plantlive_nevertell",
                null,
                "plantlive"
        );

        // When
        rabbitConsumer.start();

        // TODO More happy path stuff...
    }

    // TODO more!
    // TODO Is this the best way of testing RabbitConsumer?

}
