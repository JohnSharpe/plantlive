package com.jsharpe.plantlive.resources;

import com.jsharpe.plantlive.UnitTest;
import com.jsharpe.plantlive.api.Summary;
import com.jsharpe.plantlive.consume.PasswordHasher;
import com.jsharpe.plantlive.exceptions.IllegalPasswordException;
import com.jsharpe.plantlive.models.Detail;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.MockRepository;
import com.jsharpe.plantlive.resources.date.YesterdayDateSupplier;
import com.jsharpe.plantlive.views.StandardView;
import com.jsharpe.plantlive.views.SummaryView;
import io.dropwizard.views.View;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.ws.rs.WebApplicationException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Category(UnitTest.class)
public class OutResourceTest {

    private final MockRepository mockRepository;
    private final OutResource outResource;

    public OutResourceTest() {
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
    public void testGetJsonSummaryForNoPlantSpecified() {
        // Given

        // When
        this.outResource.getSummaryJson(null);

        // Then (exception)
    }

    @Test(expected = WebApplicationException.class)
    public void testGetJsonSummaryForNoPlantInExistence() {
        // Given

        // When
        this.outResource.getSummaryJson(UUID.randomUUID());

        // Then (exception)
    }

    @Test
    public void testGetJsonSummaryForAPlant() throws IllegalPasswordException {
        // Given
        final UUID userId = UUID.randomUUID();
        final Set<Plant> plants = new HashSet<>();
        plants.add(new Plant(1, userId, PasswordHasher.hash("password"), "cactus"));
        final Set<Detail> details = new HashSet<>();
        details.add(new Detail(1, 1, new Date(), 10, 20, 30, 40));
        details.add(new Detail(2, 1, new Date(), 20, 30, 40, 50));
        this.mockRepository.populate(plants, details);

        // When
        final Summary summary = outResource.getSummaryJson(userId);

        // Then
        Assert.assertEquals(15, summary.getTemperature(), 0.00000001);
        Assert.assertEquals(25, summary.getHumidity(), 0.00000001);
        Assert.assertEquals(35, summary.getLight(), 0.00000001);
        Assert.assertEquals(45, summary.getConductivity(), 0.00000001);
    }

    @Test
    public void testGetHtmlSummaryForNoPlantSpecified() {
        // Given

        // When
        final View view = this.outResource.getSummaryHtml(null);

        // Then
        Assert.assertNotNull(view);
        Assert.assertTrue(view instanceof StandardView);
        final StandardView standardView = (StandardView) view;
        Assert.assertFalse(standardView.isPlantNotFound());
    }

    @Test
    public void testGetHtmlSummaryForNoPlantInExistence() {
        // Given

        // When
        final View view = this.outResource.getSummaryHtml(UUID.randomUUID().toString());

        // Then
        Assert.assertNotNull(view);
        Assert.assertTrue(view instanceof StandardView);
        final StandardView standardView = (StandardView) view;
        Assert.assertTrue(standardView.isPlantNotFound());
    }

    @Test
    public void testGetHtmlSummaryForAPlant() throws IllegalPasswordException {
        // Given
        final UUID userId = UUID.randomUUID();
        final Set<Plant> plants = new HashSet<>();
        plants.add(new Plant(1, userId, PasswordHasher.hash("password"), "cactus"));
        final Set<Detail> details = new HashSet<>();
        details.add(new Detail(1, 1, new Date(), 10, 20, 30, 40));
        details.add(new Detail(2, 1, new Date(), 20, 30, 40, 50));
        this.mockRepository.populate(plants, details);

        // When
        final View view = outResource.getSummaryHtml(userId.toString());

        // Then
        Assert.assertNotNull(view);
        Assert.assertTrue(view instanceof SummaryView);
        final SummaryView summaryView = (SummaryView) view;
        Assert.assertNotNull(summaryView.getSummary());
        final Summary summary = summaryView.getSummary();
        Assert.assertEquals(15, summary.getTemperature(), 0.000001);
        Assert.assertEquals(25, summary.getHumidity(), 0.000001);
        Assert.assertEquals(35, summary.getLight(), 0.000001);
        Assert.assertEquals(45, summary.getConductivity(), 0.000001);
    }

}
