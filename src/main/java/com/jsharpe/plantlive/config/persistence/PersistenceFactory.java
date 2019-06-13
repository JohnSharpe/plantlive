package com.jsharpe.plantlive.config.persistence;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jsharpe.plantlive.repositories.RepositoryWrapper;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.setup.Environment;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = NopPersistenceFactory.class)
public interface PersistenceFactory extends Discoverable {

    DataSourceFactory getDatabase();

    RepositoryWrapper getRepositories(final Environment environment);

}
