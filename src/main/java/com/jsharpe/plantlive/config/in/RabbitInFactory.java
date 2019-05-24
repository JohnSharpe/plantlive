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
    @Min(0) // 0 = keep forever?
    private final Integer retentionHours;

    // TODO Any other consumer configuration here

    @JsonCreator
    public RabbitInFactory(@JsonProperty("retentionHours") Integer retentionHours) {
        this.retentionHours = retentionHours;
    }

    @Override
    public void initialise(Environment environment, RepositoryWrapper repositoryWrapper) throws Exception {

        final InService inService = new InService(
                repositoryWrapper.getPlantRepository(),
                repositoryWrapper.getDetailRepository(),
                this.retentionHours
        );

        final RabbitConsumer rabbitConsumer = new RabbitConsumer(inService);
        // TODO Get a healthcheck in here!
        environment.lifecycle().manage(rabbitConsumer);
    }

}
