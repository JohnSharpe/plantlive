package com.jsharpe.plantlive.models;

import java.util.Date;

public class Detail {

    private final long id;
    private final long plantId;

    private final Date inTimestamp;
    private final int temperature;
    private final int humidity;
    private final int light;
    private final int conductivity;

    public Detail(long id, long plantId, Date inTimestamp, int temperature, int humidity, int light, int conductivity) {
        this.id = id;
        this.plantId = plantId;
        this.inTimestamp = inTimestamp;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
        this.conductivity = conductivity;
    }

    public long getId() {
        return id;
    }

    public long getPlantId() {
        return plantId;
    }

    public Date getInTimestamp() {
        return inTimestamp;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getLight() {
        return light;
    }

    public int getConductivity() {
        return conductivity;
    }

}
