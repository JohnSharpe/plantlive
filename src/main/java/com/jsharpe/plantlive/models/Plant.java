package com.jsharpe.plantlive.models;

public class Plant {

    private final long id;
    private final String password;
    private final String type;
    private final String label;

    public Plant(long id, String password, String type, String label) {
        this.id = id;
        this.password = password;
        this.type = type;
        this.label = label;
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

    public String getLabel() {
        return label;
    }

}
