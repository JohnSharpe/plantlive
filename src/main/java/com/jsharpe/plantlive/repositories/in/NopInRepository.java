package com.jsharpe.plantlive.repositories.in;

import com.jsharpe.plantlive.models.Plant;

import java.util.Date;
import java.util.Optional;

public class NopInRepository implements InRepository {

    @Override
    public Optional<Plant> getPlant(long id) {
        return Optional.empty();
    }

    @Override
    public int saveDetail(long plantId, Date inTimestamp, int temperature, int humidity, int light, int conductivity) {
        return 0;
    }

}
