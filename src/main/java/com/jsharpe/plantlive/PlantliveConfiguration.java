package com.jsharpe.plantlive;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jsharpe.plantlive.config.in.InFactory;
import com.jsharpe.plantlive.config.out.OutFactory;
import com.jsharpe.plantlive.config.persistence.PersistenceFactory;
import com.jsharpe.plantlive.repositories.RepositoryWrapper;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class PlantliveConfiguration extends Configuration {

    private final PersistenceFactory persistenceFactory;
    private final InFactory inFactory;
    private final OutFactory outFactory;

    @JsonCreator
    public PlantliveConfiguration(
            @JsonProperty("persistence") @Valid @NotNull PersistenceFactory persistenceFactory,
            @JsonProperty("in") @Valid @NotNull InFactory inFactory,
            @JsonProperty("out") @Valid @NotNull OutFactory outFactory
    ) {
        this.persistenceFactory = persistenceFactory;
        this.inFactory = inFactory;
        this.outFactory = outFactory;
    }

    public void initialise(final Environment environment) {

        final RepositoryWrapper repositories = this.persistenceFactory.get(environment);
        // TODO Consider passing the necessary repositories explicitly
        // TODO Also consider a ReadRepository and a WriteRepository for both entity types
        // TODO e.g. the inFactory doesn't actually write to Plants or read from Details
        this.inFactory.initialise(environment, repositories);
        this.outFactory.initialise(environment, repositories);

    }

}
