package com.jsharpe.plantlive.config.out;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jsharpe.plantlive.repositories.RepositoryWrapper;
import com.jsharpe.plantlive.repositories.out.OutRepository;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.setup.Environment;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = NopOutFactory.class)
public interface OutFactory extends Discoverable {

    void initialise(final Environment environment, final OutRepository outRepository);

}
