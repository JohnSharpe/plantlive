package com.jsharpe.plantlive.config.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jsharpe.plantlive.repositories.RepositoryWrapper;
import com.jsharpe.plantlive.repositories.in.InRepository;
import com.jsharpe.plantlive.repositories.out.OutRepository;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("sql")
public class SqlPersistenceFactory implements PersistenceFactory {

    private final DataSourceFactory database;

    @JsonCreator
    public SqlPersistenceFactory(
            @JsonProperty("database") @Valid @NotNull DataSourceFactory database
    ) {
        this.database = database;
    }

    @Override
    public DataSourceFactory getDatabase() {
        return this.database;
    }

    @Override
    public RepositoryWrapper getRepositories(Environment environment) {
        // Also registers a healthcheck!
        final Jdbi jdbi = new JdbiFactory().build(environment, this.database, "sql");

        // Create
        final InRepository inRepository = jdbi.onDemand(InRepository.class);
        final OutRepository outRepository = jdbi.onDemand(OutRepository.class);

        // Register
        environment.jersey().register(inRepository);
        environment.jersey().register(outRepository);

        return new RepositoryWrapper(inRepository, outRepository);
    }
}
