package com.jsharpe.plantlive.consume.rabbit;

import com.jsharpe.plantlive.IntegrationTest;
import com.jsharpe.plantlive.consume.InService;
import com.jsharpe.plantlive.consume.PasswordHasher;
import com.jsharpe.plantlive.consume.rabbit.RabbitConsumer;
import com.jsharpe.plantlive.models.Detail;
import com.jsharpe.plantlive.repositories.MockDetailRepository;
import com.jsharpe.plantlive.repositories.MockPlantRepository;
import com.rabbitmq.client.AuthenticationFailureException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeoutException;

@Category(IntegrationTest.class)
public class RabbitConsumerTest {

    private final MockPlantRepository plantRepository;
    private final MockDetailRepository detailRepository;
    private final LocalRabbitPublisher localRabbitPublisher;
    private final InService inService;

    public RabbitConsumerTest() throws IOException, TimeoutException {
        this.plantRepository = new MockPlantRepository();
        this.detailRepository = new MockDetailRepository();

        this.localRabbitPublisher = new LocalRabbitPublisher(
                "127.0.0.1",
                5672,
                "guest",
                "guest",
                null,
                "plantlive"
        );

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
                "plantlive"
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
                "plantlive"
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

    @Test
    public void testOnePublish() throws Exception {
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
        rabbitConsumer.start();

        this.plantRepository.save(PasswordHasher.hash("1234"), "cactus", "test");

        // When
        this.localRabbitPublisher.publish("1;1234;2;3;4;5");
        Thread.sleep(500);

        // Then
        final Set<Detail> details = this.detailRepository.get();
        Assert.assertEquals(1, details.size());

        final Detail detail = details.iterator().next();
        Assert.assertEquals(2, detail.getTemperature());
        Assert.assertEquals(3, detail.getHumidity());
        Assert.assertEquals(4, detail.getLight());
        Assert.assertEquals(5, detail.getConductivity());
    }

    @Test
    public void testRecoverFromPublish() throws Exception {
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
        rabbitConsumer.start();

        this.plantRepository.save(PasswordHasher.hash("1234"), "cactus", "test");

        // When
        // No such plant!
        this.localRabbitPublisher.publish("2;1234;2;3;4;5");
        Thread.sleep(500);
        this.localRabbitPublisher.publish("1;1234;2;3;4;5");
        Thread.sleep(500);

        // Then
        final Set<Detail> details = this.detailRepository.get();
        Assert.assertEquals(1, details.size());

        final Detail detail = details.iterator().next();
        Assert.assertEquals(2, detail.getTemperature());
        Assert.assertEquals(3, detail.getHumidity());
        Assert.assertEquals(4, detail.getLight());
        Assert.assertEquals(5, detail.getConductivity());
    }

    // TODO more!
    // TODO Is this the best way of testing RabbitConsumer?

}
