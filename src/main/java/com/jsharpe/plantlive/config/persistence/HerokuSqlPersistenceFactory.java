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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

@JsonTypeName("heroku-sql")
public class HerokuSqlPersistenceFactory implements PersistenceFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(HerokuSqlPersistenceFactory.class);

    private final DataSourceFactory database;

    @JsonCreator
    public HerokuSqlPersistenceFactory(
            @JsonProperty("databaseUrlKey") @Valid @NotNull String databaseUrlKey,
            @JsonProperty("database") @Valid @NotNull DataSourceFactory database
    ) {
        // databaseUrl looks like postgres://user:pass@host.location.com:1234/databasename
        final String databaseUrl = System.getenv(databaseUrlKey);
        LOGGER.info("Initialising with {}", databaseUrl);
        final String[] userPass = databaseUrl
                .substring(11) // Remove postgres://
                .split("@")[0] // Take everything before the @
                .split(":"); // Split on the colon

        LOGGER.debug(Arrays.toString(userPass));

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
