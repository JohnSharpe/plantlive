package com.jsharpe.plantlive.repositories;

import io.dropwizard.testing.FixtureHelpers;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

public abstract class SqlUtils {

    public static final String FIXTURES_ROOT = "fixtures/sql/";

    private SqlUtils() {
    }

    public static void executeSeedSql(final Jdbi jdbi, final String filename) {
        final String script = FixtureHelpers.fixture(filename);
        try (final Handle h = jdbi.open()) {
            h.createScript(script).execute();
        }
    }

}
