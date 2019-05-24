package com.jsharpe.plantlive.repositories.detail;

import java.util.Date;

public class NopDetailRepository implements DetailRepository {

    @Override
    public int save(long plantId, Date timestamp, int temperature, int humidity, int light, int conductivity) {
        return 0;
    }

}
