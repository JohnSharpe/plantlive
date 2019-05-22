package com.jsharpe.plantlive.config.out;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jsharpe.plantlive.repositories.RepositoryWrapper;
import io.dropwizard.setup.Environment;

@JsonTypeName("nop")
public class NopOutFactory implements OutFactory {

    @Override
    public void initialise(Environment environment, RepositoryWrapper repositoryWrapper) {

    }

}
