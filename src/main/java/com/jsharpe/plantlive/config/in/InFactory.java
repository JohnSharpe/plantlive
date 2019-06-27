package com.jsharpe.plantlive.config.in;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jsharpe.plantlive.repositories.in.InRepository;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.setup.Environment;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = NopInFactory.class)
public interface InFactory extends Discoverable {

    void initialise(final Environment environment, final InRepository inRepository) throws Exception;

}
