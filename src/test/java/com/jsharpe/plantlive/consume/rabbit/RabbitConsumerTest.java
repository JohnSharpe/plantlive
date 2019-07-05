package com.jsharpe.plantlive.consume.rabbit;

import com.jsharpe.plantlive.IntegrationTest;
import com.jsharpe.plantlive.consume.InService;
import com.jsharpe.plantlive.consume.PasswordHasher;
import com.jsharpe.plantlive.models.Detail;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.MockRepository;
import com.rabbitmq.client.AuthenticationFailureException;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

@Category(IntegrationTest.class)
public class RabbitConsumerTest {

    private final MockRepository mockRepository;
    private final LocalRabbitPublisher localRabbitPublisher;
    private final InService inService;

    public RabbitConsumerTest() throws IOException, TimeoutException {
        this.mockRepository = new MockRepository();

        this.localRabbitPublisher = new LocalRabbitPublisher(
                "127.0.0.1",
                5672,
                "guest",
                "guest",
                null,
                "plantlive"
        );

        this.inService = new InService(
                this.mockRepository.getPlantOutRepository(),
                this.mockRepository.getDetailInRepository(),
                24
        );
    }

    @Before
    public void before() {
        this.mockRepository.clear();
    }

    @Test(expected = AuthenticationFailureException.class)
    public void testNoSuchUser() throws Exception {
        // Given
        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("some-nonexistent-user");
        connectionFactory.setPassword("some-nonexistent-user's-password");

        final RabbitConsumer rabbitConsumer = new RabbitConsumer(
                this.inService,
                connectionFactory,
                "plantlive"
        );

        // When
        rabbitConsumer.start();
    }

    @Test(expected = AuthenticationFailureException.class)
    public void testIncorrectPassword() throws Exception {
        // Given
        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("plantlive_rabbituser");
        connectionFactory.setPassword("some-wrong-password");

        final RabbitConsumer rabbitConsumer = new RabbitConsumer(
                this.inService,
                connectionFactory,
                "plantlive"
        );

        // When
        rabbitConsumer.start();
    }

    @Test(expected = IOException.class)
    public void testNoSuchQueue() throws Exception {
        // Given
        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("plantlive_rabbituser");
        connectionFactory.setPassword("plantlive_nevertell");

        final RabbitConsumer rabbitConsumer = new RabbitConsumer(
                this.inService,
                connectionFactory,
                "plantlifffe"
        );

        // When
        rabbitConsumer.start();
    }

    @Test
    public void testOnePublish() throws Exception {
        // Given
        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("plantlive_rabbituser");
        connectionFactory.setPassword("plantlive_nevertell");

        final RabbitConsumer rabbitConsumer = new RabbitConsumer(
                this.inService,
                connectionFactory,
                "plantlive"
        );
        rabbitConsumer.start();

        final Set<Plant> givenPlants = new HashSet<>();
        givenPlants.add(new Plant(-1, PasswordHasher.hash("1234"), "cactus"));
        this.mockRepository.populate(givenPlants, null);

        // When
        this.localRabbitPublisher.publish("1;1234;2;3;4;5");
        Thread.sleep(500);

        // Then
        final Set<Detail> details = this.mockRepository.getDetails();
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
        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("plantlive_rabbituser");
        connectionFactory.setPassword("plantlive_nevertell");

        final RabbitConsumer rabbitConsumer = new RabbitConsumer(
                this.inService,
                connectionFactory,
                "plantlive"
        );
        rabbitConsumer.start();

        final Set<Plant> givenPlants = new HashSet<>();
        givenPlants.add(new Plant(-1, PasswordHasher.hash("1234"), "cactus"));
        this.mockRepository.populate(givenPlants, null);

        // When
        // No such plant!
        this.localRabbitPublisher.publish("2;1234;2;3;4;5");
        Thread.sleep(500);
        this.localRabbitPublisher.publish("1;1234;2;3;4;5");
        Thread.sleep(500);

        // Then
        final Set<Detail> details = this.mockRepository.getDetails();
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
