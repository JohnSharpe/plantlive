package com.jsharpe.plantlive.models;

import java.beans.ConstructorProperties;
import java.util.UUID;

public class Plant {

    private final long id;
    private final UUID userId;
    private final String password;
    private final String type;

    @ConstructorProperties({"id", "user_id", "password", "type"})
    public Plant(long id, UUID userId, String password, String type) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

}
