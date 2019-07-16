package com.jsharpe.plantlive.config.masterPassword;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("env")
public class EnvMasterPasswordCheck implements MasterPasswordCheck {

    private final String masterPassword;

    @JsonCreator
    public EnvMasterPasswordCheck(
            @JsonProperty("key") @Valid @NotNull String key
    ) {

        final String masterPassword = System.getenv(key);

        if (StringUtils.isNotBlank(masterPassword)) {
            this.masterPassword = masterPassword;
        } else {
            throw new RuntimeException("No environment variable [" + key + "] for masterPassword");
        }
    }

    @Override
    public boolean matches(String candidate) {
        return this.masterPassword.equals(candidate);
    }
}
