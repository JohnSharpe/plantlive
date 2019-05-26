package com.jsharpe.plantlive.consume;

import com.jsharpe.plantlive.exceptions.ConsumeException;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.detail.DetailRepository;
import com.jsharpe.plantlive.repositories.plant.PlantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Optional;

/**
 * A container for the domain-logic of writing data from a consumer to a repository.
 * More testable than doing this work in each consumer implementation
 */
public class InService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InService.class);

    private final PlantRepository plantRepository;
    private final DetailRepository detailRepository;

    private final int retentionHours;

    public InService(
            final PlantRepository plantRepository,
            final DetailRepository detailRepository,
            final int retentionHours
    ) {
        this.plantRepository = plantRepository;
        this.detailRepository = detailRepository;

        this.retentionHours = retentionHours;
    }

    public void write(
            final long plantId,
            final String password,
            final Date timestamp,
            final int temperature,
            final int humidity,
            final int light,
            final int conductivity
    ) throws ConsumeException {

        final Optional<Plant> optionalPlant = this.plantRepository.get(plantId);

        if (!optionalPlant.isPresent()) {
            LOGGER.warn("No plant with id {}", plantId);
            throw new ConsumeException();
        }

        final Plant plant = optionalPlant.get();

        if (!PasswordHasher.verify(plant.getPassword(), password)) {
            LOGGER.warn("Password {} is incorrect for plant with id {}", password, plantId);
            throw new ConsumeException();
        }

        // We're going to trust the date

        final boolean temperatureValid = verify(temperature);
        final boolean humidityValid = verify(humidity);
        final boolean lightValid = verify(light);
        final boolean conductivityValid = verify(conductivity);

        if (temperatureValid && humidityValid && lightValid && conductivityValid) {
            this.detailRepository.save(plantId, timestamp, temperature, humidity, light, conductivity);
        } else {
            LOGGER.warn(
                    "Data invalid for detail - plantId: {}, timestamp: {}, temp: {}, hum: {}, light: {}, cond: {}",
                    plantId,
                    timestamp,
                    temperature,
                    humidity,
                    light,
                    conductivity
            );
            throw new ConsumeException();
        }

    }

    // TODO This could do with being pulled out and unit tested, it is likely to change
    // TODO Also, different verification is likely to take place on the different numbers...
    // TODO e.g temperature probably won't be 0 to infinity
    private boolean verify(int candidate) {
        return candidate >= 0;
    }

}
