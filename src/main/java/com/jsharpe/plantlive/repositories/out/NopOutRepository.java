package com.jsharpe.plantlive.repositories.out;

import com.jsharpe.plantlive.api.Summary;

import java.util.Date;
import java.util.Optional;

public class NopOutRepository implements OutRepository {

    @Override
    public Optional<Summary> getSummary(long id, Date since) {
        return Optional.empty();
    }

}
