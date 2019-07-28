package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.UnitTest;
import com.jsharpe.plantlive.api.Summary;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.details.in.NopDetailInRepository;
import com.jsharpe.plantlive.repositories.details.out.NopDetailOutRepository;
import com.jsharpe.plantlive.repositories.plants.in.NopPlantInRepository;
import com.jsharpe.plantlive.repositories.plants.out.NopPlantOutRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Category(UnitTest.class)
public class NopRepositoryTest {

    @Test
    public void testNopPlantInRepositorySave() {
        // Given
        final NopPlantInRepository plantInRepository = new NopPlantInRepository();

        // When
        final int rows = plantInRepository.save(null, null, null);

        // Then
        Assert.assertEquals(0, rows);
    }

    @Test
    public void testNopPlantInRepositoryUpdateType() {
        // Given
        final NopPlantInRepository plantInRepository = new NopPlantInRepository();

        // When
        final int rows = plantInRepository.updateType(null,0);

        // Then
        Assert.assertEquals(0, rows);
    }

    @Test
    public void testNopPlantOutRepository() {
        // Given
        final NopPlantOutRepository plantOutRepository = new NopPlantOutRepository();

        // When
        final Optional<Plant> plantOptional = plantOutRepository.getByUserId(UUID.randomUUID());

        // Then
        Assert.assertFalse(plantOptional.isPresent());
    }

    @Test
    public void testNopDetailInRepository() {
        // Given
        final NopDetailInRepository detailInRepository = new NopDetailInRepository();

        // When
        final int rows = detailInRepository.save(1, new Date(), 2, 3, 4, 5);

        // Then
        Assert.assertEquals(0, rows);
    }

    @Test
    public void testNopDetailOutRepository() {
        // Given
        final NopDetailOutRepository detailOutRepository = new NopDetailOutRepository();

        // When
        final Summary summary = detailOutRepository.getSummary(1, new Date());

        // Then
        Assert.assertNotNull(summary);
        Assert.assertEquals(0, summary.getTemperature(), 0.000001);
        Assert.assertEquals(0, summary.getHumidity(), 0.000001);
        Assert.assertEquals(0, summary.getLight(), 0.000001);
        Assert.assertEquals(0, summary.getConductivity(), 0.000001);
    }

}
