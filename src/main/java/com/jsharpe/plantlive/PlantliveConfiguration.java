package com.jsharpe.plantlive;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jsharpe.plantlive.config.in.InFactory;
import com.jsharpe.plantlive.config.masterPassword.MasterPasswordCheck;
import com.jsharpe.plantlive.config.out.OutFactory;
import com.jsharpe.plantlive.config.persistence.PersistenceFactory;
import com.jsharpe.plantlive.repositories.RepositoryWrapper;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Environment;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class PlantliveConfiguration extends Configuration {

    @Valid
    @NotNull
    private final MasterPasswordCheck masterPasswordCheck;

    @Valid
    @NotNull
    private final PersistenceFactory persistenceFactory;

    @Valid
    @NotNull
    private final InFactory inFactory;

    @Valid
    @NotNull
    private final OutFactory outFactory;

    @JsonCreator
    public PlantliveConfiguration(
            @JsonProperty("masterPassword") MasterPasswordCheck masterPasswordCheck,
            @JsonProperty("persistence") PersistenceFactory persistenceFactory,
            @JsonProperty("in") InFactory inFactory,
            @JsonProperty("out") OutFactory outFactory
    ) {
        this.masterPasswordCheck = masterPasswordCheck;
        this.persistenceFactory = persistenceFactory;
        this.inFactory = inFactory;
        this.outFactory = outFactory;
    }

    public DataSourceFactory getDatabase() {
        return this.persistenceFactory.getDatabase();
    }

    public void initialise(final Environment environment) throws Exception {

        final RepositoryWrapper repositories = this.persistenceFactory.getRepositories(environment);
        this.inFactory.initialise(environment, repositories.getPlantOutRepository(), repositories.getDetailInRepository());
        this.outFactory.initialise(
                environment,
                this.masterPasswordCheck,
                repositories.getPlantInRepository(),
                repositories.getPlantOutRepository(),
                repositories.getDetailOutRepository()
        );

    }

}
