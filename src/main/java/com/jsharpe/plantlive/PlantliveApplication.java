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

        bootstrap.addBundle(new MigrationsBundle<PlantliveConfiguration>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(PlantliveConfiguration plantliveConfiguration) {
                return plantliveConfiguration.getDatabase();
            }
        });

    }

    @Override
    public void run(final PlantliveConfiguration configuration, final Environment environment) throws Exception {
        configuration.initialise(environment);
    }

}
