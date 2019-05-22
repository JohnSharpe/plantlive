package com.jsharpe.plantlive.config.in;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jsharpe.plantlive.repositories.RepositoryWrapper;
import io.dropwizard.setup.Environment;

@JsonTypeName("nop")
public class NopInFactory implements InFactory {

    @Override
    public void initialise(Environment environment, RepositoryWrapper repositoryWrapper) {

    }

}
