package com.jsharpe.plantlive.repositories.plants.out;

import com.jsharpe.plantlive.models.Plant;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.Optional;

public interface PlantOutRepository {

    @SqlQuery("SELECT * FROM plants WHERE id = ?")
    @RegisterConstructorMapper(Plant.class)
    Optional<Plant> get(long id);

}
