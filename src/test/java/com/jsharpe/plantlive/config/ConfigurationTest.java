package com.jsharpe.plantlive.config;

import com.jsharpe.plantlive.PlantliveConfiguration;
import com.jsharpe.plantlive.UnitTest;
import com.jsharpe.plantlive.config.in.InFactory;
import com.jsharpe.plantlive.config.in.NopInFactory;
import com.jsharpe.plantlive.config.out.NopOutFactory;
import com.jsharpe.plantlive.config.out.OutFactory;
import com.jsharpe.plantlive.config.persistence.NopPersistenceFactory;
import com.jsharpe.plantlive.config.persistence.PersistenceFactory;
import com.jsharpe.plantlive.repositories.RepositoryWrapper;
import com.jsharpe.plantlive.repositories.details.in.NopDetailInRepository;
import com.jsharpe.plantlive.repositories.details.out.NopDetailOutRepository;
import com.jsharpe.plantlive.repositories.plants.in.NopPlantInRepository;
import com.jsharpe.plantlive.repositories.plants.out.NopPlantOutRepository;
import io.dropwizard.setup.Environment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

@Category(UnitTest.class)
public class ConfigurationTest {

    private final Environment environment = Mockito.mock(Environment.class);

    @Test
    public void testNopInitialisation() throws Exception {
        // Given
        final PersistenceFactory persistenceFactory = new NopPersistenceFactory();
        final InFactory inFactory = new NopInFactory();
        final OutFactory outFactory = new NopOutFactory();
        final PlantliveConfiguration plantliveConfiguration = new PlantliveConfiguration(
                persistenceFactory,
                inFactory,
                outFactory
        );

        // When
        plantliveConfiguration.initialise(environment);

        // Then
        Assert.assertNull(plantliveConfiguration.getDatabase());
    }

    @Test
    public void testMockedInitialisation() throws Exception {
        // Given
        final PersistenceFactory persistenceFactory = Mockito.mock(PersistenceFactory.class);
        Mockito.when(persistenceFactory.getRepositories(Mockito.any())).thenReturn(
                new RepositoryWrapper(
                        new NopPlantInRepository(),
                        new NopPlantOutRepository(),
                        new NopDetailInRepository(),
                        new NopDetailOutRepository()
                )
        );

        final InFactory inFactory = Mockito.mock(InFactory.class);
        final OutFactory outFactory = Mockito.mock(OutFactory.class);

        final PlantliveConfiguration plantliveConfiguration = new PlantliveConfiguration(
                persistenceFactory,
                inFactory,
                outFactory
        );

        // When
        plantliveConfiguration.initialise(environment);

        // Then
        Mockito.verify(inFactory, Mockito.times(1)).initialise(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(outFactory, Mockito.times(1)).initialise(
                Mockito.any(), Mockito.any(), Mockito.any()
        );

    }

}
