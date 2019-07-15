package com.jsharpe.plantlive.config.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jsharpe.plantlive.repositories.RepositoryWrapper;
import com.jsharpe.plantlive.repositories.details.in.DetailInRepository;
import com.jsharpe.plantlive.repositories.details.out.DetailOutRepository;
import com.jsharpe.plantlive.repositories.plants.in.PlantInRepository;
import com.jsharpe.plantlive.repositories.plants.out.PlantOutRepository;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("heroku-sql")
public class HerokuSqlPersistenceFactory implements PersistenceFactory {

    private final DataSourceFactory database;

    @JsonCreator
    public HerokuSqlPersistenceFactory(
            @JsonProperty("databaseUrl") @Valid @NotNull String databaseUrl,
            @JsonProperty("database") @Valid @NotNull DataSourceFactory database
    ) {
        // databaseUrl looks like postgres://user:pass@host.location.com:1234/databasename
        final String[] userPass = databaseUrl.substring(11).split("@")[0].split(":");
        database.setUser(userPass[0]);
        database.setPassword(userPass[1]);
        database.setUrl("jdbc:postgresql://" + databaseUrl.split("@")[1]);
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
        final PlantInRepository plantInRepository = jdbi.onDemand(PlantInRepository.class);
        final PlantOutRepository plantOutRepository = jdbi.onDemand(PlantOutRepository.class);
        final DetailInRepository detailInRepository = jdbi.onDemand(DetailInRepository.class);
        final DetailOutRepository detailOutRepository = jdbi.onDemand(DetailOutRepository.class);

        // Register
        environment.jersey().register(plantInRepository);
        environment.jersey().register(plantOutRepository);
        environment.jersey().register(detailInRepository);
        environment.jersey().register(detailOutRepository);

        return new RepositoryWrapper(plantInRepository, plantOutRepository, detailInRepository, detailOutRepository);
    }

}
