package com.jsharpe.plantlive.repositories.details.in;

import java.util.Date;

public class NopDetailInRepository implements DetailInRepository {

    @Override
    public int save(
            final long plantId,
            final Date inTimestamp,
            final double temperature,
            final double humidity,
            final double light,
            final double conductivity
    ) {
        return 0;
    }

}
