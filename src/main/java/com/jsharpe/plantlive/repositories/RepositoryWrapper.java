package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.repositories.detail.DetailRepository;
import com.jsharpe.plantlive.repositories.plant.PlantRepository;

public class RepositoryWrapper {

    private final PlantRepository plantRepository;
    private final DetailRepository detailRepository;

    public RepositoryWrapper(PlantRepository plantRepository, DetailRepository detailRepository) {
        this.plantRepository = plantRepository;
        this.detailRepository = detailRepository;
    }

    public PlantRepository getPlantRepository() {
        return plantRepository;
    }

    public DetailRepository getDetailRepository() {
        return detailRepository;
    }

}
