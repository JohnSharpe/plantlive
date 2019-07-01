package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.models.Detail;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.in.InRepository;

import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

public class MockInRepository extends MockRepository implements InRepository {

    public MockInRepository() {
        super();
    }

    public MockInRepository(final Set<Plant> plants, final Set<Detail> details) {
        super(plants, details);
    }

    @Override
    public Optional<Plant> getPlant(long id) {
        for (Plant plant : plants) {
            if (plant.getId() == id) {
                return Optional.of(plant);
            }
        }
        return Optional.empty();
    }

    @Override
    public int saveDetail(long plantId, Date inTimestamp, int temperature, int humidity, int light, int conductivity) throws SQLException {

        if (!this.getPlant(plantId).isPresent()) {
            throw new SQLException();
        }

        final Detail detail = new Detail(this.lastDetailId.incrementAndGet(), plantId, inTimestamp, temperature, humidity, light, conductivity);
        this.details.add(detail);
        return 1;
    }

}
