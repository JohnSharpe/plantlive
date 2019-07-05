package com.jsharpe.plantlive.repositories.plants.out;

import com.jsharpe.plantlive.models.Plant;

import java.util.Optional;

public class NopPlantOutRepository implements PlantOutRepository {

    @Override
    public Optional<Plant> get(long id) {
        return Optional.empty();
    }

}
