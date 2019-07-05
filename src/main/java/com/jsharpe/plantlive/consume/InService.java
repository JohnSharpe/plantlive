package com.jsharpe.plantlive.consume;

import com.jsharpe.plantlive.exceptions.ConsumeException;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.details.in.DetailInRepository;
import com.jsharpe.plantlive.repositories.plants.out.PlantOutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

/**
 * A container for the domain-logic of writing data from a consumer to a repository.
 * More testable than doing this work in each consumer implementation
 */
public class InService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InService.class);

    private final PlantOutRepository plantOutRepository;
    private final DetailInRepository detailInRepository;

    // TODO Could remove detail entries older than retentionHours
    private final int retentionHours;

    public InService(
            final PlantOutRepository plantOutRepository,
            final DetailInRepository detailInRepository,
            final int retentionHours
    ) {
        this.plantOutRepository = plantOutRepository;
        this.detailInRepository = detailInRepository;
        this.retentionHours = retentionHours;
    }

    public void write(
            final long plantId,
            final String password,
            final Date inTimestamp,
            final int temperature,
            final int humidity,
            final int light,
            final int conductivity
    ) throws ConsumeException {

        final Optional<Plant> optionalPlant = this.plantOutRepository.get(plantId);

        if (!optionalPlant.isPresent()) {
            final String problem = String.format("No plant with id [%d]", plantId);
            LOGGER.warn(problem);
            throw new ConsumeException(problem);
        }

        final Plant plant = optionalPlant.get();

        if (!PasswordHasher.verify(plant.getPassword(), password)) {
            final String problem = String.format("Password [%s] is incorrect for plant with id [%d]", password, plantId);
            LOGGER.warn(problem);
            throw new ConsumeException(problem);
        }

        // We're going to trust the date

        // TODO Ugh.
        final boolean temperatureValid = verify(temperature);
        final boolean humidityValid = verify(humidity);
        final boolean lightValid = verify(light);
        final boolean conductivityValid = verify(conductivity);

        if (temperatureValid && humidityValid && lightValid && conductivityValid) {
            try {
                this.detailInRepository.save(plantId, inTimestamp, temperature, humidity, light, conductivity);
            } catch (SQLException e) {
                LOGGER.warn("SQL problem saving detail", e);
            }
        } else {
            final String problem = String.format(
                    "Data invalid for detail - plantId: [%d], inTimestamp: [%s], temp: [%d], hum: [%d], light: [%d], cond: [%d]",
                    plantId, inTimestamp, temperature, humidity, light, conductivity
            );
            LOGGER.warn(problem);
            throw new ConsumeException(problem);
        }

    }

    // TODO This could do with being pulled out and unit tested, it is likely to change
    // TODO Also, different verification is likely to take place on the different numbers...
    // TODO e.g temperature probably won't be 0 to infinity
    private boolean verify(int candidate) {
        return candidate >= 0;
    }

}
