package com.jsharpe.plantlive.config.in;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jsharpe.plantlive.repositories.in.InRepository;
import io.dropwizard.setup.Environment;

@JsonTypeName("nop")
public class NopInFactory implements InFactory {

    @Override
    public void initialise(final Environment environment, final InRepository inRepository) {

    }

}
