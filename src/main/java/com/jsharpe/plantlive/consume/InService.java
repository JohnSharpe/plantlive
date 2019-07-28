package com.jsharpe.plantlive.consume;

import com.jsharpe.plantlive.exceptions.ConsumeException;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.details.in.DetailInRepository;
import com.jsharpe.plantlive.repositories.plants.in.PlantInRepository;
import com.jsharpe.plantlive.repositories.plants.out.PlantOutRepository;
import org.apache.commons.lang3.StringUtils;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * A container for the domain-logic of writing data from a consumer to a repository.
 * More testable than doing this work in each consumer implementation
 */
public class InService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InService.class);

    private final PlantOutRepository plantOutRepository;
    private final PlantInRepository plantInRepository;
    private final DetailInRepository detailInRepository;

    public InService(
            final PlantOutRepository plantOutRepository,
            final PlantInRepository plantInRepository,
            final DetailInRepository detailInRepository
    ) {
        this.plantOutRepository = plantOutRepository;
        this.plantInRepository = plantInRepository;
        this.detailInRepository = detailInRepository;
    }

    public void write(
            final UUID userId,
            final String type,
            final String password,
            final Date inTimestamp,
            final double temperature,
            final double humidity,
            final double light,
            final double conductivity
    ) throws ConsumeException {

        final Optional<Plant> optionalPlant = this.plantOutRepository.getByUserId(userId);

        if (!optionalPlant.isPresent()) {
            final String problem = String.format("No plant with user id [%s]", userId);
            LOGGER.warn(problem);
            throw new ConsumeException(problem);
        }

        final Plant plant = optionalPlant.get();

        if (!PasswordHasher.verify(plant.getPassword(), password)) {
            final String problem = String.format("Password [%s] is incorrect for plant with user id [%s]", password, userId);
            LOGGER.warn(problem);
            throw new ConsumeException(problem);
        }

        // If type is different, get it in
        // TODO Validate type when there's something to validate against.
        // TODO Possible an enum of acceptable plants
        if (StringUtils.isNotBlank(type) && !type.equalsIgnoreCase(plant.getType())) {
            this.plantInRepository.updateType(type, plant.getId());
        }

        // We're going to trust the date

        // TODO Ugh.
        final boolean temperatureValid = verify(temperature);
        final boolean humidityValid = verify(humidity);
        final boolean lightValid = verify(light);
        final boolean conductivityValid = verify(conductivity);

        if (temperatureValid && humidityValid && lightValid && conductivityValid) {
            try {
                this.detailInRepository.save(plant.getId(), inTimestamp, temperature, humidity, light, conductivity);
            } catch (UnableToExecuteStatementException e) {
                LOGGER.warn("SQL problem saving detail", e);
            }
        } else {
            final String problem = String.format(
                    "Data invalid for detail - plantId: [%d], inTimestamp: [%s], temp: [%f], hum: [%f], light: [%f], cond: [%f]",
                    plant.getId(), inTimestamp, temperature, humidity, light, conductivity
            );
            LOGGER.warn(problem);
            throw new ConsumeException(problem);
        }

    }

    // TODO This could do with being pulled out and unit tested, it is likely to change
    // TODO Also, different verification is likely to take place on the different numbers...
    // TODO e.g temperature probably won't be 0 to infinity
    private boolean verify(double candidate) {
        return candidate >= 0;
    }

}
