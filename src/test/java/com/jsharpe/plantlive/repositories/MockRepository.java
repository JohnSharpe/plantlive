package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.models.Detail;
import com.jsharpe.plantlive.models.Plant;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class MockRepository {

    final AtomicLong lastPlantId = new AtomicLong(0);
    final Set<Plant> plants = new HashSet<>();

    final AtomicLong lastDetailId = new AtomicLong(0);
    final Set<Detail> details = new HashSet<>();

    MockRepository() {
    }

    MockRepository(final Set<Plant> plants, final Set<Detail> details) {
        this.populate(plants, details);
    }

    public void clear() {
        this.plants.clear();
        this.lastPlantId.set(0);

        this.details.clear();
        this.lastDetailId.set(0);
    }

    public void populate(final Set<Plant> plants, final Set<Detail> details) {
        if (plants != null) {
            plants.forEach(plant -> this.plants.add(new Plant(
                    this.lastPlantId.incrementAndGet(),
                    plant.getPassword(),
                    plant.getType()
            )));
        }

        if (details != null) {
            details.forEach(detail -> this.details.add(new Detail(
                    this.lastDetailId.incrementAndGet(),
                    detail.getPlantId(),
                    detail.getInTimestamp(),
                    detail.getTemperature(),
                    detail.getHumidity(),
                    detail.getLight(),
                    detail.getConductivity()
            )));
        }
    }

    public Set<Plant> getPlants() {
        return this.plants;
    }

    public Set<Detail> getDetails() {
        return this.details;
    }

}
