package com.jsharpe.plantlive.repositories.plant;

import com.jsharpe.plantlive.models.Plant;

import java.util.Optional;

public interface PlantRepository {

    Optional<Plant> get(long id);

    int save(String password, String type, String label);

}
