package com.jsharpe.plantlive.repositories.plants.in;

import java.util.UUID;

public class NopPlantInRepository implements PlantInRepository {

    @Override
    public int updateType(String type, long id) {
        return 0;
    }

    @Override
    public int save(UUID userId, String password, String type) {
        return 0;
    }

}
