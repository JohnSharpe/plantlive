package com.jsharpe.plantlive.consume;

import com.jsharpe.plantlive.IntegrationTest;
import com.jsharpe.plantlive.consume.rabbit.RabbitConsumer;
import com.jsharpe.plantlive.repositories.MockDetailRepository;
import com.jsharpe.plantlive.repositories.MockPlantRepository;
import com.rabbitmq.client.AuthenticationFailureException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

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

    // TODO more!
    // TODO Is this the best way of testing RabbitConsumer?

}
