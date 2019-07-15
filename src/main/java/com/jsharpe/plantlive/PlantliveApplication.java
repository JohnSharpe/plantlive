package com.jsharpe.plantlive;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

public class PlantliveApplication extends Application<PlantliveConfiguration> {

    public static void main(String[] args) throws Exception {
        new PlantliveApplication().run(args);
    }

    @Override
    public String getName() {
        return "Plantlive";
    }

    @Override
    public void initialize(final Bootstrap<PlantliveConfiguration> bootstrap) {

        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(true)
                )
        );

        // This is a shame. I didn't want to have to expose the database configuration here.
        // If we're using NopPersistence (or maybe non-sql in the far future) we wouldn't add the bundle at all.
        bootstrap.addBundle(new MigrationsBundle<PlantliveConfiguration>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(PlantliveConfiguration configuration) {
                return configuration.getDatabase();
            }
        });

        // TODO Might want to override getViewConfiguration()
        bootstrap.addBundle(new ViewBundle<>());

    }

    @Override
    public void run(final PlantliveConfiguration configuration, final Environment environment) throws Exception {
        configuration.initialise(environment);
    }

}
