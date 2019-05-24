package com.jsharpe.plantlive;

import io.dropwizard.Application;
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

    }

    @Override
    public void run(final PlantliveConfiguration configuration, final Environment environment) throws Exception {
        configuration.initialise(environment);
    }

}
