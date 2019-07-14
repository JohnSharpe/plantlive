package com.jsharpe.plantlive.repositories.plants.out;

import com.jsharpe.plantlive.models.Plant;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.Optional;
import java.util.UUID;

public interface PlantOutRepository {

    @SqlQuery("SELECT * FROM plants WHERE user_id = ?")
    @RegisterConstructorMapper(Plant.class)
    Optional<Plant> getByUserId(UUID uuid);

}
