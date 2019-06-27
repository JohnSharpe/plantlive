package com.jsharpe.plantlive.models;

import java.beans.ConstructorProperties;

public class Plant {

    private final long id;
    private final String password;
    private final String type;

    @ConstructorProperties({"id", "password", "type"})
    public Plant(long id, String password, String type) {
        this.id = id;
        this.password = password;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

}
