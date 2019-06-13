package com.jsharpe.plantlive.config.persistence;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jsharpe.plantlive.repositories.RepositoryWrapper;
import com.jsharpe.plantlive.repositories.detail.NopDetailRepository;
import com.jsharpe.plantlive.repositories.plant.NopPlantRepository;
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
                new NopPlantRepository(),
                new NopDetailRepository()
        );
    }

}
