package com.jsharpe.plantlive.config.masterPassword;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = NopMasterPasswordCheck.class)
public interface MasterPasswordCheck extends Discoverable {

    boolean matches(final String candidate);

}
