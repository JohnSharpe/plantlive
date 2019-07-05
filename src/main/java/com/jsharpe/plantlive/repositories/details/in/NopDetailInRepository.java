package com.jsharpe.plantlive.repositories.details.in;

import java.sql.SQLException;
import java.util.Date;

public class NopDetailInRepository implements DetailInRepository {

    @Override
    public int save(long plantId, Date inTimestamp, int temperature, int humidity, int light, int conductivity) throws SQLException {
        return 0;
    }

}
