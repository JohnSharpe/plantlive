package com.jsharpe.plantlive.config.out;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jsharpe.plantlive.repositories.details.out.DetailOutRepository;
import com.jsharpe.plantlive.repositories.plants.out.PlantOutRepository;
import io.dropwizard.setup.Environment;

@JsonTypeName("nop")
public class NopOutFactory implements OutFactory {

    @Override
    public void initialise(
            final Environment environment,
            final PlantOutRepository plantOutRepository,
            final DetailOutRepository detailOutRepository) {

    }

}
