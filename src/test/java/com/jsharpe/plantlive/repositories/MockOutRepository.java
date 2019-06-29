package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.api.Summary;
import com.jsharpe.plantlive.models.Detail;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.out.OutRepository;

import java.util.Date;
import java.util.Optional;

public class MockOutRepository extends MockRepository implements OutRepository {

    @Override
    public Optional<Summary> getSummary(long id, Date since) {

        int tempTotal = 0;
        int tempCount = 0;

        int humiTotal = 0;
        int humiCount = 0;

        int lightTotal = 0;
        int lightCount = 0;

        int condTotal = 0;
        int condCount = 0;

        Plant plant = null;
        for (Plant plantCandidate : this.plants) {
            if (plantCandidate.getId() == id) {
                plant = plantCandidate;
                break;
            }
        }

        if (plant == null) {
            return Optional.empty();
        }

        for (Detail detail : this.details) {
            if (detail.getPlantId() == id && detail.getInTimestamp().after(since)) {
                tempTotal += detail.getTemperature();
                tempCount++;

                humiTotal += detail.getHumidity();
                humiCount++;

                lightTotal += detail.getLight();
                lightCount++;

                condTotal += detail.getConductivity();
                condCount++;
            }
        }

        return Optional.of(
                new Summary(
                        plant.getId(),
                        plant.getType(),
                        tempTotal / tempCount,
                        humiTotal / humiCount,
                        lightTotal / lightCount,
                        condTotal / condCount
                )
        );
    }
}
