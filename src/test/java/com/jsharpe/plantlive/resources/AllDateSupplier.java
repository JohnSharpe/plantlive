package com.jsharpe.plantlive.resources;

import com.jsharpe.plantlive.resources.date.DateSupplier;

import java.util.Date;

public class AllDateSupplier implements DateSupplier {
    @Override
    public Date getDate() {
        // The epoch!
        return new Date(0);
    }
}
