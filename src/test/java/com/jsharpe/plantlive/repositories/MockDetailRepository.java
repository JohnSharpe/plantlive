package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.models.Detail;
import com.jsharpe.plantlive.repositories.detail.DetailRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class MockDetailRepository implements DetailRepository {

    private final AtomicLong lastId = new AtomicLong(0);
    private final Set<Detail> details = new HashSet<>();

    @Override
    public int save(long plantId, Date timestamp, int temperature, int humidity, int light, int conductivity) {
        this.details.add(
                new Detail(lastId.incrementAndGet(), plantId, timestamp, temperature, humidity, light, conductivity)
        );
        return 1;
    }

    public Set<Detail> get() {
        return details;
    }

    public void clear() {
        this.details.clear();
        this.lastId.set(0);
    }

}
