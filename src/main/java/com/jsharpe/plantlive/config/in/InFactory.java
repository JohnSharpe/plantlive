package com.jsharpe.plantlive.config.in;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jsharpe.plantlive.repositories.details.in.DetailInRepository;
import com.jsharpe.plantlive.repositories.plants.in.PlantInRepository;
import com.jsharpe.plantlive.repositories.plants.out.PlantOutRepository;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.setup.Environment;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = NopInFactory.class)
public interface InFactory extends Discoverable {

    void initialise(
            final Environment environment,
            final PlantOutRepository plantOutRepository,
            final PlantInRepository plantInRepository,
            final DetailInRepository detailInRepository
    ) throws Exception;

}
