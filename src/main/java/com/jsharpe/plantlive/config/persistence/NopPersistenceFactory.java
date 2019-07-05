package com.jsharpe.plantlive.config.persistence;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jsharpe.plantlive.repositories.RepositoryWrapper;
import com.jsharpe.plantlive.repositories.details.in.NopDetailInRepository;
import com.jsharpe.plantlive.repositories.details.out.NopDetailOutRepository;
import com.jsharpe.plantlive.repositories.plants.in.NopPlantInRepository;
import com.jsharpe.plantlive.repositories.plants.out.NopPlantOutRepository;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Environment;

@JsonTypeName("nop")
public class NopPersistenceFactory implements PersistenceFactory {

    @Override
    public DataSourceFactory getDatabase() {
        return null;
    }

    @Override
    public RepositoryWrapper getRepositories(Environment environment) {
        return new RepositoryWrapper(
                new NopPlantInRepository(),
                new NopPlantOutRepository(),
                new NopDetailInRepository(),
                new NopDetailOutRepository()
        );
    }

}
