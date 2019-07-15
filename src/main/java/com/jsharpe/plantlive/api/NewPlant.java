package com.jsharpe.plantlive.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NewPlant {

    private final String masterPassword;
    private final String password;
    private final String type;

    @JsonCreator
    public NewPlant(
            @JsonProperty("masterPassword") String masterPassword,
            @JsonProperty("password") String password,
            @JsonProperty("type") String type
    ) {
        this.masterPassword = masterPassword;
        this.password = password;
        this.type = type;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

}
