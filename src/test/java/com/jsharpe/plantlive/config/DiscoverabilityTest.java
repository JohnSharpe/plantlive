package com.jsharpe.plantlive.config;

import com.google.common.collect.ImmutableList;
import com.jsharpe.plantlive.UnitTest;
import com.jsharpe.plantlive.config.in.NopInFactory;
import com.jsharpe.plantlive.config.in.RabbitInFactory;
import com.jsharpe.plantlive.config.out.NopOutFactory;
import com.jsharpe.plantlive.config.persistence.NopPersistenceFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(UnitTest.class)
public class DiscoverabilityTest {

    @Test
    public void testDiscoverables() {
        // Given
        final ImmutableList<Class<?>> discoverables = new DiscoverableSubtypeResolver()
                .getDiscoveredSubtypes();

        // Then
        MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(NopPersistenceFactory.class));
        // TODO MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(SqlPersistenceFactory.class));

        MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(NopInFactory.class));
        MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(RabbitInFactory.class));

        MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(NopOutFactory.class));
        // TODO MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(HttpOutFactory.class));
    }

}
