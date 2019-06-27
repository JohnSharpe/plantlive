package com.jsharpe.plantlive.repositories.out;

import com.jsharpe.plantlive.api.Summary;

import java.util.Date;
import java.util.Optional;

public interface OutRepository {

    Optional<Summary> getSummary(long id, Date since);

}
