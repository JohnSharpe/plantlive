package com.jsharpe.plantlive.config.masterPassword;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("nop")
public class NopMasterPasswordCheck implements MasterPasswordCheck {

    @Override
    public boolean matches(final String candidate) {
        return false;
    }

}
