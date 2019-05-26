package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.plant.PlantRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class MockPlantRepository implements PlantRepository {

    private final AtomicLong lastId = new AtomicLong(0);
    private final Set<Plant> plants = new HashSet<>();

    public MockPlantRepository() {
    }

    public MockPlantRepository(String password, String type, String label) {
        this.save(password, type, label);
    }

    @Override
    public Optional<Plant> get(long id) {
        for (Plant plant : plants) {
            if (plant.getId() == id) {
                return Optional.of(plant);
            }
        }
        return Optional.empty();
    }

    @Override
    public int save(String password, String type, String label) {
        plants.add(
                new Plant(lastId.incrementAndGet(), password, type, label)
        );
        return 1;
    }

    public void clear() {
        this.plants.clear();
        this.lastId.set(0);
    }

}
