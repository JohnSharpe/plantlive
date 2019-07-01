package com.jsharpe.plantlive.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;

/**
 * This class is a summary of a given plant right now.
 * The 4 numbers (thlc) are averages over the past x hours (configurable).
 * This might be serialized to JSON or translated to an HTML UI.
 */
public class Summary {

    // TODO this might eventually be a uuid (i.e. obscuring database state)
//    private final long id;

    // TODO this might eventually be an enum
//    private final String type;

    private final double temperature;
    private final double humidity;
    private final double light;
    private final double conductivity;

    // TODO include ranges/parameters for bad, medium, good, perfect etc.

    /**
     * These annotations enable Dropwizard-blessed serialization testing, but we don't accept Summaries externally.
     * Constraints mentioned below are to be maintained by the database.
     * <p>
     * //@param id           the plant's unique id
     * //@param type         the plant's not-null type. Will later help us to determine how a plant 'should' be.
     *
     * @param temperature  the average temperature of the air around the plant
     * @param humidity     the average humidity of the air around the plant
     * @param light        the average light level the plant is exposed to
     * @param conductivity the average conductivity (wetness) of the soil
     */
    @ConstructorProperties({"avg_temperature", "avg_humidity", "avg_light", "avg_conductivity"})
    @JsonCreator
    public Summary(
//            @JsonProperty("id") long id,
//            @JsonProperty("type") String type,
            @JsonProperty("temperature") double temperature,
            @JsonProperty("humidity") double humidity,
            @JsonProperty("light") double light,
            @JsonProperty("conductivity") double conductivity
    ) {
//        this.id = id;
//        this.type = type;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
        this.conductivity = conductivity;
    }

//    @JsonProperty("id")
//    public long getId() {
//        return id;
//    }

//    @JsonProperty("type")
//    public String getType() {
//        return type;
//    }

    @JsonProperty("temperature")
    public double getTemperature() {
        return temperature;
    }

    @JsonProperty("humidity")
    public double getHumidity() {
        return humidity;
    }

    @JsonProperty("light")
    public double getLight() {
        return light;
    }

    @JsonProperty("conductivity")
    public double getConductivity() {
        return conductivity;
    }

}
