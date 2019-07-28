package com.jsharpe.plantlive.config;

import com.jsharpe.plantlive.PlantliveApplication;
import io.dropwizard.Application;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationTest {

    @Test
    public void testGetName() {
        // Given
        final Application application = new PlantliveApplication();

        // When
        final String appName = application.getName();

        // Then (gotta get those coverage numbers up)
        Assert.assertEquals("Plantlive", appName);
    }

}
