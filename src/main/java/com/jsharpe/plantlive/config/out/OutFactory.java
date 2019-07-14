package com.jsharpe.plantlive.config.out;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jsharpe.plantlive.repositories.details.out.DetailOutRepository;
import com.jsharpe.plantlive.repositories.plants.in.PlantInRepository;
import com.jsharpe.plantlive.repositories.plants.out.PlantOutRepository;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.setup.Environment;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = NopOutFactory.class)
public interface OutFactory extends Discoverable {

    void initialise(
            final Environment environment,
            final String masterPassword,
            final PlantInRepository plantInRepository,
            final PlantOutRepository plantOutRepository,
            final DetailOutRepository detailOutRepository
    );

}
