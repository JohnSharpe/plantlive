package com.jsharpe.plantlive.models;

import java.util.Date;

public class Detail {

    private final long id;
    private final long plantId;

    private final Date inTimestamp;
    private final double temperature;
    private final double humidity;
    private final double light;
    private final double conductivity;

    public Detail(
            final long id,
            final long plantId,
            final Date inTimestamp,
            final double temperature,
            final double humidity,
            final double light,
            final double conductivity
    ) {
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

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getLight() {
        return light;
    }

    public double getConductivity() {
        return conductivity;
    }

}
