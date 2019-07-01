package com.jsharpe.plantlive.config.persistence;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jsharpe.plantlive.repositories.RepositoryWrapper;
import com.jsharpe.plantlive.repositories.in.NopInRepository;
import com.jsharpe.plantlive.repositories.out.NopOutRepository;
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
                new NopInRepository(),
                new NopOutRepository()
        );
    }

}
