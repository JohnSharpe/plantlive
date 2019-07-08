package com.jsharpe.plantlive.repositories.details.in;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.sql.SQLException;
import java.util.Date;

public interface DetailInRepository {

    @SqlUpdate("INSERT INTO details (plant_id, in_timestamp, temperature, humidity, light, conductivity) VALUES (?, ?, ?, ?, ?, ?)")
    int save(long plantId, Date inTimestamp, double temperature, double humidity, double light, double conductivity) throws SQLException;

}
