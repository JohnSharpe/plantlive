package com.jsharpe.plantlive.repositories.details.out;

import com.jsharpe.plantlive.api.Summary;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.Date;

public interface DetailOutRepository {

    @SqlQuery("SELECT MAX(d.in_timestamp) max_in_timestamp, AVG(d.temperature) avg_temperature, AVG(d.humidity) avg_humidity, AVG(d.light) avg_light, AVG(d.conductivity) avg_conductivity " +
            "FROM plants p INNER JOIN details d ON p.id = d.plant_id " +
            "WHERE p.id = :id AND d.in_timestamp >= :since")
    @RegisterConstructorMapper(Summary.class)
    Summary getSummary(@Bind("id") long id, @Bind("since") Date since);

}
