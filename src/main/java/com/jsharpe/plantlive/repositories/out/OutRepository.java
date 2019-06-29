package com.jsharpe.plantlive.repositories.out;

import com.jsharpe.plantlive.api.Summary;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.Date;
import java.util.Optional;

public interface OutRepository {

    // TODO TODO TODO
    @SqlQuery("SELECT 1, 'cactus', 1, 2, 3, 4")
    @RegisterConstructorMapper(Summary.class)
    Optional<Summary> getSummary(long id, Date since);

}
