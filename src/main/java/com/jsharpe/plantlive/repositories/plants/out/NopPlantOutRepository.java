package com.jsharpe.plantlive.repositories.plants.out;

import com.jsharpe.plantlive.models.Plant;

import java.util.Optional;
import java.util.UUID;

public class NopPlantOutRepository implements PlantOutRepository {

    @Override
    public Optional<Plant> getByUserId(UUID userId) {
        return Optional.empty();
    }

}
