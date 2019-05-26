package com.jsharpe.plantlive.config.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jsharpe.plantlive.consume.InService;
import com.jsharpe.plantlive.consume.rabbit.RabbitConsumer;
import com.jsharpe.plantlive.repositories.RepositoryWrapper;
import io.dropwizard.setup.Environment;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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

    @Valid
    @NotNull
    @Min(0) // 0 = keep forever?
    private final Integer retentionHours;

    // TODO Any other consumer configuration here

    @JsonCreator
    public RabbitInFactory(
            @JsonProperty("host") String host,
            @JsonProperty("port") Integer port,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("vhost") String vhost,
            @JsonProperty("queue") String queue,
            @JsonProperty("retentionHours") Integer retentionHours
    ) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.vhost = vhost;
        this.queue = queue;
        this.retentionHours = retentionHours;
    }

    @Override
    public void initialise(Environment environment, RepositoryWrapper repositoryWrapper) throws Exception {

        final InService inService = new InService(
                repositoryWrapper.getPlantRepository(),
                repositoryWrapper.getDetailRepository(),
                this.retentionHours
        );

        final RabbitConsumer rabbitConsumer = new RabbitConsumer(
                inService,
                this.host,
                this.port,
                this.username,
                this.password,
                this.vhost,
                this.queue
        );

        // TODO Get a healthcheck in here!
        environment.lifecycle().manage(rabbitConsumer);
    }

}
