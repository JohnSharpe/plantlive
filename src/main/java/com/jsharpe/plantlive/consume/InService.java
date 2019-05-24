package com.jsharpe.plantlive.consume;

import com.jsharpe.plantlive.repositories.detail.DetailRepository;
import com.jsharpe.plantlive.repositories.plant.PlantRepository;

import java.util.Date;

/**
 * A container for the domain-logic of writing data from a consumer to a repository.
 * More testable than doing this work in each consumer implementation
 */
public class InService {

    private final PlantRepository plantRepository;
    private final DetailRepository detailRepository;

    private final int retentionHours;

    public InService(
            final PlantRepository plantRepository,
            final DetailRepository detailRepository,
            final int retentionHours
    ) {
        this.plantRepository = plantRepository;
        this.detailRepository = detailRepository;

        this.retentionHours = retentionHours;
    }

    public void write(
            final String plantId,
            final String password,
            final Date timestamp,
            final int temperature,
            final int humidity,
            final int light,
            final int conductivity
    ) {
        // TODO Logic!
    }

}
