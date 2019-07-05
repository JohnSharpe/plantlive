package com.jsharpe.plantlive.resources;

import com.jsharpe.plantlive.UnitTest;
import com.jsharpe.plantlive.api.Summary;
import com.jsharpe.plantlive.consume.PasswordHasher;
import com.jsharpe.plantlive.exceptions.IllegalPasswordException;
import com.jsharpe.plantlive.models.Detail;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.MockRepository;
import com.jsharpe.plantlive.resources.date.YesterdayDateSupplier;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.ws.rs.WebApplicationException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Category(UnitTest.class)
public class ResourceTest {

    private final MockRepository mockRepository;
    private final OutResource outResource;

    public ResourceTest() {
        this.mockRepository = new MockRepository();
        this.outResource = new OutResource(
                this.mockRepository.getPlantOutRepository(),
                this.mockRepository.getDetailOutRepository(),
                new YesterdayDateSupplier()
        );
    }

    @After
    public void tearDown() {
        this.mockRepository.clear();
    }

    @Test(expected = WebApplicationException.class)
    public void testGetSummaryForNoPlantSpecified() {
        // Given

        // When
        this.outResource.getSummaryJson(null);

        // Then (exception)
    }

    @Test(expected = WebApplicationException.class)
    public void testGetSummaryForNoPlantInExistence() {
        // Given

        // When
        this.outResource.getSummaryJson(1L);

        // Then (exception)
    }

    @Test
    public void testGetSummaryForAPlant() throws IllegalPasswordException {
        // Given
        final Set<Plant> plants = new HashSet<>();
        plants.add(new Plant(1, PasswordHasher.hash("password"), "cactus"));
        final Set<Detail> details = new HashSet<>();
        details.add(new Detail(1, 1, new Date(), 10, 20, 30, 40));
        details.add(new Detail(2, 1, new Date(), 20, 30, 40, 50));
        this.mockRepository.populate(plants, details);

        // When
        final Summary summary = outResource.getSummaryJson(1L);

        // Then
        Assert.assertEquals(15, summary.getTemperature(), 0.00000001);
        Assert.assertEquals(25, summary.getHumidity(), 0.00000001);
        Assert.assertEquals(35, summary.getLight(), 0.00000001);
        Assert.assertEquals(45, summary.getConductivity(), 0.00000001);
    }

}
