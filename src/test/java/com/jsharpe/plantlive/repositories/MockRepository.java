package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.api.Summary;
import com.jsharpe.plantlive.models.Detail;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.details.in.DetailInRepository;
import com.jsharpe.plantlive.repositories.details.out.DetailOutRepository;
import com.jsharpe.plantlive.repositories.plants.in.PlantInRepository;
import com.jsharpe.plantlive.repositories.plants.out.PlantOutRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class MockRepository {

    private final AtomicLong LAST_PLANT_ID = new AtomicLong(0);
    private final Set<Plant> PLANTS = new HashSet<>();

    private final AtomicLong LAST_DETAIL_ID = new AtomicLong(0);
    private final Set<Detail> DETAILS = new HashSet<>();

     private final PlantInRepository plantInRepository;
    private final PlantOutRepository plantOutRepository;
    private final DetailInRepository detailInRepository;
    private final DetailOutRepository detailOutRepository;

    public MockRepository() {
        this.clear();
         this.plantInRepository = new PlantIn(LAST_PLANT_ID, PLANTS);
        this.plantOutRepository = new PlantOut(PLANTS);
        this.detailInRepository = new DetailIn(LAST_DETAIL_ID, DETAILS);
        this.detailOutRepository = new DetailOut(PLANTS, DETAILS);
    }

    public void clear() {
        LAST_PLANT_ID.set(0);
        PLANTS.clear();
        LAST_DETAIL_ID.set(0);
        DETAILS.clear();
    }

    public void populate(final Set<Plant> plants, final Set<Detail> details) {
        if (plants != null) {
            plants.forEach(plant -> PLANTS.add(new Plant(
                    LAST_PLANT_ID.incrementAndGet(),
                    plant.getUserId(),
                    plant.getPassword(),
                    plant.getType()
            )));
        }

        if (details != null) {
            details.forEach(detail -> DETAILS.add(new Detail(
                    LAST_DETAIL_ID.incrementAndGet(),
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
        return PLANTS;
    }

    public Set<Detail> getDetails() {
        return DETAILS;
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

    public static class PlantIn implements PlantInRepository {

        private final AtomicLong lastPlantId;
        private final Set<Plant> plants;

        PlantIn(AtomicLong lastPlantId, Set<Plant> plants) {
            this.lastPlantId = lastPlantId;
            this.plants = plants;
        }

        @Override
        public int save(UUID userId, String password, String type) {
            final Plant plant = new Plant(lastPlantId.incrementAndGet(), userId, password, type);
            this.plants.add(plant);
            return 1;
        }
    }

    public static class PlantOut implements PlantOutRepository {

        private final Set<Plant> plants;

        PlantOut(Set<Plant> plants) {
            this.plants = plants;
        }

        @Override
        public Optional<Plant> getByUserId(UUID userId) {
            for (final Plant plant : this.plants) {
                if (plant.getUserId().equals(userId)) {
                    return Optional.of(plant);
                }
            }

            return Optional.empty();
        }
    }

    public static class DetailIn implements DetailInRepository {

        private final AtomicLong lastDetailId;
        private final Set<Detail> details;

        DetailIn(AtomicLong lastDetailId, Set<Detail> details) {
            this.lastDetailId = lastDetailId;
            this.details = details;
        }

        @Override
        public int save(long plantId, Date inTimestamp, double temperature, double humidity, double light, double conductivity) {
            details.add(new Detail(lastDetailId.incrementAndGet(), plantId, inTimestamp, temperature, humidity, light, conductivity));
            return 1;
        }
    }

    public static class DetailOut implements DetailOutRepository {

        private final Set<Plant> plants;
        private final Set<Detail> details;

        DetailOut(Set<Plant> plants, Set<Detail> details) {
            this.plants = plants;
            this.details = details;
        }

        @Override
        public Summary getSummary(long id, Date since) {
            int tempTotal = 0;
            int tempCount = 0;

            int humiTotal = 0;
            int humiCount = 0;

            int lightTotal = 0;
            int lightCount = 0;

            int condTotal = 0;
            int condCount = 0;

            Plant plant = null;
            for (Plant plantCandidate : this.plants) {
                if (plantCandidate.getId() == id) {
                    plant = plantCandidate;
                    break;
                }
            }

            if (plant == null) {
                return new Summary(0, 0, 0, 0);
            }

            for (Detail detail : this.details) {
                if (detail.getPlantId() == id && detail.getInTimestamp().after(since)) {
                    tempTotal += detail.getTemperature();
                    tempCount++;

                    humiTotal += detail.getHumidity();
                    humiCount++;

                    lightTotal += detail.getLight();
                    lightCount++;

                    condTotal += detail.getConductivity();
                    condCount++;
                }
            }

            return new Summary(
                    tempTotal / tempCount,
                    humiTotal / humiCount,
                    lightTotal / lightCount,
                    condTotal / condCount
            );
        }
    }

}
