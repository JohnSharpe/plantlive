package com.jsharpe.plantlive.repositories.details.out;

import com.jsharpe.plantlive.api.Summary;

import java.util.Date;

public class NopDetailOutRepository implements DetailOutRepository {

    private static final Summary EMPTY_SUMMARY = new Summary(0, 0, 0, 0);

    @Override
    public Summary getSummary(long id, Date since) {
        return EMPTY_SUMMARY;
    }

}
