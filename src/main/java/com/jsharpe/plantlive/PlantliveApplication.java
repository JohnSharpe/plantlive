package com.jsharpe.plantlive;

import io.dropwizard.Application;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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

        // This is a shame. I didn't want to have to expose the database configuration here.
        // If we're using NopPersistence (or maybe non-sql in the far future) we wouldn't add the bundle at all.
        bootstrap.addBundle(new MigrationsBundle<PlantliveConfiguration>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(PlantliveConfiguration configuration) {
                return configuration.getDatabase();
            }
        });

    }

    @Override
    public void run(final PlantliveConfiguration configuration, final Environment environment) throws Exception {
        configuration.initialise(environment);
    }

}
