package com.jsharpe.plantlive.config.out;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jsharpe.plantlive.config.masterPassword.MasterPasswordCheck;
import com.jsharpe.plantlive.repositories.details.out.DetailOutRepository;
import com.jsharpe.plantlive.repositories.plants.in.PlantInRepository;
import com.jsharpe.plantlive.repositories.plants.out.PlantOutRepository;
import com.jsharpe.plantlive.resources.OutResource;
import com.jsharpe.plantlive.resources.PlantResource;
import com.jsharpe.plantlive.resources.date.DateSupplier;
import com.jsharpe.plantlive.resources.date.YesterdayDateSupplier;
import io.dropwizard.setup.Environment;

@JsonTypeName("http")
public class HttpOutFactory implements OutFactory {

    @Override
    public void initialise(
            final Environment environment,
            final MasterPasswordCheck masterPasswordCheck,
            final PlantInRepository plantInRepository,
            final PlantOutRepository plantOutRepository,
            final DetailOutRepository detailOutRepository
    ) {
        final DateSupplier dateSupplier = new YesterdayDateSupplier();
        final OutResource outResource = new OutResource(plantOutRepository, detailOutRepository, dateSupplier);
        environment.jersey().register(outResource);

        final PlantResource plantResource = new PlantResource(masterPasswordCheck, plantInRepository);
        environment.jersey().register(plantResource);
    }

}
