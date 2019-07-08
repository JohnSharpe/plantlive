package com.jsharpe.plantlive.config.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jsharpe.plantlive.consume.InService;
import com.jsharpe.plantlive.consume.rabbit.RabbitConsumer;
import com.jsharpe.plantlive.health.RabbitHealthCheck;
import com.jsharpe.plantlive.repositories.details.in.DetailInRepository;
import com.jsharpe.plantlive.repositories.plants.out.PlantOutRepository;
import com.rabbitmq.client.ConnectionFactory;
import io.dropwizard.setup.Environment;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("rabbit")
public class RabbitInFactory implements InFactory {

    // TODO Rabbit-specific configuration here
    @Valid
    @NotNull
    private final String host;

    @Valid
    @NotNull
    private final Integer port;

    @Valid
    @NotNull
    private final String username;

    @Valid
    @NotNull
    private final String password;

    private final String vhost;

    @Valid
    @NotNull
    private final String queue;

    // TODO Any other consumer configuration here

    @JsonCreator
    public RabbitInFactory(
            @JsonProperty("host") String host,
            @JsonProperty("port") Integer port,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("vhost") String vhost,
            @JsonProperty("queue") String queue
    ) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.vhost = vhost;
        this.queue = queue;
    }

    @Override
    public void initialise(
            final Environment environment,
            final PlantOutRepository plantOutRepository,
            final DetailInRepository detailInRepository
    ) {

        final InService inService = new InService(
                plantOutRepository,
                detailInRepository
        );

        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        if (StringUtils.isNotBlank(vhost)) {
            connectionFactory.setVirtualHost(vhost);
        }

        final RabbitHealthCheck rabbitHealthcheck = new RabbitHealthCheck(connectionFactory, this.queue);
        environment.healthChecks().register("rabbit", rabbitHealthcheck);

        final RabbitConsumer rabbitConsumer = new RabbitConsumer(
                inService,
                connectionFactory,
                this.queue
        );

        environment.lifecycle().manage(rabbitConsumer);
    }

}
