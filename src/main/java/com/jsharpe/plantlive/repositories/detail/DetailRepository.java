package com.jsharpe.plantlive.repositories.detail;

import java.util.Date;

public interface DetailRepository {

    int save(long plantId, Date timestamp, int temperature, int humidity, int light, int conductivity);

}
