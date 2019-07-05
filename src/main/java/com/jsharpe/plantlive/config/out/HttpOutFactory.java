package com.jsharpe.plantlive.config.out;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jsharpe.plantlive.repositories.out.OutRepository;
import com.jsharpe.plantlive.resources.OutResource;
import com.jsharpe.plantlive.resources.date.DateSupplier;
import com.jsharpe.plantlive.resources.date.YesterdayDateSupplier;
import io.dropwizard.setup.Environment;

@JsonTypeName("http")
public class HttpOutFactory implements OutFactory {

    @Override
    public void initialise(Environment environment, OutRepository outRepository) {
        final DateSupplier dateSupplier = new YesterdayDateSupplier();
        final OutResource outResource = new OutResource(outRepository, dateSupplier);
        environment.jersey().register(outResource);
    }

}
