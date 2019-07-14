package com.jsharpe.plantlive.config;

import com.jsharpe.plantlive.PlantliveConfiguration;
import com.jsharpe.plantlive.UnitTest;
import com.jsharpe.plantlive.config.in.InFactory;
import com.jsharpe.plantlive.config.in.NopInFactory;
import com.jsharpe.plantlive.config.out.NopOutFactory;
import com.jsharpe.plantlive.config.out.OutFactory;
import com.jsharpe.plantlive.config.persistence.NopPersistenceFactory;
import com.jsharpe.plantlive.config.persistence.PersistenceFactory;
import io.dropwizard.setup.Environment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

@Category(UnitTest.class)
public class ConfigurationTest {

    private final Environment environment = Mockito.mock(Environment.class);

    @Test
    public void testInitialisation() throws Exception {
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

}
