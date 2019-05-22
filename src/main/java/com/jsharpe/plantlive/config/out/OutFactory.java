package com.jsharpe.plantlive.config.out;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jsharpe.plantlive.repositories.RepositoryWrapper;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.setup.Environment;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = NopOutFactory.class)
public interface OutFactory extends Discoverable {

    void initialise(final Environment environment, final RepositoryWrapper repositoryWrapper);

}
