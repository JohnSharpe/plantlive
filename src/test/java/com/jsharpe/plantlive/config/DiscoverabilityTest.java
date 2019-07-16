package com.jsharpe.plantlive.config;

import com.google.common.collect.ImmutableList;
import com.jsharpe.plantlive.UnitTest;
import com.jsharpe.plantlive.config.in.NopInFactory;
import com.jsharpe.plantlive.config.in.RabbitInFactory;
import com.jsharpe.plantlive.config.masterPassword.DirectMasterPasswordCheck;
import com.jsharpe.plantlive.config.masterPassword.EnvMasterPasswordCheck;
import com.jsharpe.plantlive.config.masterPassword.NopMasterPasswordCheck;
import com.jsharpe.plantlive.config.out.HttpOutFactory;
import com.jsharpe.plantlive.config.out.NopOutFactory;
import com.jsharpe.plantlive.config.persistence.HerokuSqlPersistenceFactory;
import com.jsharpe.plantlive.config.persistence.NopPersistenceFactory;
import com.jsharpe.plantlive.config.persistence.SqlPersistenceFactory;
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
        MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(NopMasterPasswordCheck.class));
        MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(DirectMasterPasswordCheck.class));
        MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(EnvMasterPasswordCheck.class));

        MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(NopPersistenceFactory.class));
        MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(SqlPersistenceFactory.class));
        MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(HerokuSqlPersistenceFactory.class));

        MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(NopInFactory.class));
        MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(RabbitInFactory.class));

        MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(NopOutFactory.class));
        MatcherAssert.assertThat(discoverables, CoreMatchers.hasItem(HttpOutFactory.class));
    }

}
