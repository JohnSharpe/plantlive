package com.jsharpe.plantlive.resources.date;

import java.util.Date;

/**
 * Some way of generating a relevant date object.
 * Possibly overkill, but useful for testing.
 */
public interface DateSupplier {

    Date getDate();

}
