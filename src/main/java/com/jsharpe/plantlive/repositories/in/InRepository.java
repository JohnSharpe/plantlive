package com.jsharpe.plantlive.repositories.in;

import com.jsharpe.plantlive.models.Plant;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

public interface InRepository {

    @SqlQuery("SELECT * FROM plants WHERE id = ?")
    @RegisterConstructorMapper(Plant.class)
    Optional<Plant> getPlant(long id);

    @SqlUpdate("INSERT INTO details (plant_id, in_timestamp, temperature, humidity, light, conductivity) VALUES (?, ?, ?, ?, ?, ?)")
    int saveDetail(long plantId, Date inTimestamp, int temperature, int humidity, int light, int conductivity) throws SQLException;

}
