package com.jsharpe.plantlive.resources.date;

import java.util.Calendar;
import java.util.Date;

public class YesterdayDateSupplier implements DateSupplier {

    @Override
    public Date getDate() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

}
