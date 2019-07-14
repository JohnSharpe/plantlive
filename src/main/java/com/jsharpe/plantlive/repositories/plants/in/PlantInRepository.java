package com.jsharpe.plantlive.repositories.plants.in;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.UUID;

public interface PlantInRepository {

    @SqlUpdate("INSERT INTO plants (user_id, password, type) VALUES (?, ?, ?)")
    int save(UUID userId, String password, String type);

}
