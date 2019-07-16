package com.jsharpe.plantlive.config.masterPassword;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("direct")
public class DirectMasterPasswordCheck implements MasterPasswordCheck {

    private final String masterPassword;

    @JsonCreator
    public DirectMasterPasswordCheck(
            @JsonProperty("value") @Valid @NotNull String value) {
        this.masterPassword = value;
    }

    @Override
    public boolean matches(String candidate) {
        return this.masterPassword.equals(candidate);
    }
}
