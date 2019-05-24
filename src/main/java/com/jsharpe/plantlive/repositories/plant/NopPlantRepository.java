package com.jsharpe.plantlive.repositories.plant;

import com.jsharpe.plantlive.models.Plant;

import java.util.Optional;

public class NopPlantRepository implements PlantRepository {

    @Override
    public Optional<Plant> get(long id) {
        return Optional.empty();
    }

    @Override
    public int save(String password, String type, String label) {
        return 0;
    }

}
