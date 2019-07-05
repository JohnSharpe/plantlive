package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.repositories.details.in.DetailInRepository;
import com.jsharpe.plantlive.repositories.details.out.DetailOutRepository;
import com.jsharpe.plantlive.repositories.plants.in.PlantInRepository;
import com.jsharpe.plantlive.repositories.plants.out.PlantOutRepository;

public class RepositoryWrapper {

    private final PlantInRepository plantInRepository;
    private final PlantOutRepository plantOutRepository;
    private final DetailInRepository detailInRepository;
    private final DetailOutRepository detailOutRepository;

    public RepositoryWrapper(
            final PlantInRepository plantInRepository,
            final PlantOutRepository plantOutRepository,
            final DetailInRepository detailInRepository,
            final DetailOutRepository detailOutRepository
    ) {
        this.plantInRepository = plantInRepository;
        this.plantOutRepository = plantOutRepository;
        this.detailInRepository = detailInRepository;
        this.detailOutRepository = detailOutRepository;
    }

    public PlantInRepository getPlantInRepository() {
        return plantInRepository;
    }

    public PlantOutRepository getPlantOutRepository() {
        return plantOutRepository;
    }

    public DetailInRepository getDetailInRepository() {
        return detailInRepository;
    }

    public DetailOutRepository getDetailOutRepository() {
        return detailOutRepository;
    }

}
