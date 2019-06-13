package com.jsharpe.plantlive.config.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jsharpe.plantlive.repositories.RepositoryWrapper;
import com.jsharpe.plantlive.repositories.detail.DetailRepository;
import com.jsharpe.plantlive.repositories.detail.NopDetailRepository;
import com.jsharpe.plantlive.repositories.plant.NopPlantRepository;
import com.jsharpe.plantlive.repositories.plant.PlantRepository;
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

        // Create TODO
        final PlantRepository plantRepository = new NopPlantRepository(); // jdbi.onDemand(PlantRepository.class)
        final DetailRepository detailRepository = new NopDetailRepository(); // jdbi.onDemand(DetailRepository.class)

        // Register
        environment.jersey().register(plantRepository);
        environment.jersey().register(detailRepository);

        return new RepositoryWrapper(plantRepository, detailRepository);
    }
}
