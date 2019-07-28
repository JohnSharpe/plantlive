package com.jsharpe.plantlive.config.in;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jsharpe.plantlive.repositories.details.in.DetailInRepository;
import com.jsharpe.plantlive.repositories.plants.in.PlantInRepository;
import com.jsharpe.plantlive.repositories.plants.out.PlantOutRepository;
import io.dropwizard.setup.Environment;

@JsonTypeName("nop")
public class NopInFactory implements InFactory {

    @Override
    public void initialise(
            final Environment environment,
            final PlantOutRepository plantOutRepository,
            final PlantInRepository plantInRepository,
            final DetailInRepository detailInRepository
    ) throws Exception {

    }
}
