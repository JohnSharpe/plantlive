package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.UnitTest;
import com.jsharpe.plantlive.repositories.details.in.DetailInRepository;
import com.jsharpe.plantlive.repositories.details.in.NopDetailInRepository;
import com.jsharpe.plantlive.repositories.details.out.DetailOutRepository;
import com.jsharpe.plantlive.repositories.details.out.NopDetailOutRepository;
import com.jsharpe.plantlive.repositories.plants.in.NopPlantInRepository;
import com.jsharpe.plantlive.repositories.plants.in.PlantInRepository;
import com.jsharpe.plantlive.repositories.plants.out.NopPlantOutRepository;
import com.jsharpe.plantlive.repositories.plants.out.PlantOutRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(UnitTest.class)
public class RepositoryWrapperTest {

    @Test
    public void testRepositoryWrapperObject() {
        // Given
        final PlantInRepository plantInRepository = new NopPlantInRepository();
        final PlantOutRepository plantOutRepository = new NopPlantOutRepository();
        final DetailInRepository detailInRepository = new NopDetailInRepository();
        final DetailOutRepository detailOutRepository = new NopDetailOutRepository();

        // When
        final RepositoryWrapper repositoryWrapper = new RepositoryWrapper(
                plantInRepository,
                plantOutRepository,
                detailInRepository,
                detailOutRepository
        );

        // Then
        Assert.assertEquals(plantInRepository, repositoryWrapper.getPlantInRepository());
        Assert.assertEquals(plantOutRepository, repositoryWrapper.getPlantOutRepository());
        Assert.assertEquals(detailInRepository, repositoryWrapper.getDetailInRepository());
        Assert.assertEquals(detailOutRepository, repositoryWrapper.getDetailOutRepository());
    }

}
