package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.IntegrationTest;
import com.jsharpe.plantlive.PlantliveApplication;
import com.jsharpe.plantlive.PlantliveConfiguration;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.in.InRepository;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.*;
import org.junit.experimental.categories.Category;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@Category(IntegrationTest.class)
public class SqlInRepositoryTest {

    @ClassRule
    public static final DropwizardAppRule<PlantliveConfiguration> RULE =
            new DropwizardAppRule<>(PlantliveApplication.class, ResourceHelpers.resourceFilePath("postgres-only.yml"));

    private static final String FIXTURES_ROOT = "fixtures/sql/";

    private static Jdbi JDBI;
    private static InRepository IN_REPOSITORY;

    @BeforeClass
    public static void setupClass() throws LiquibaseException, SQLException {
        final ManagedDataSource ds = RULE.getConfiguration()
                .getDatabase()
                .build(RULE.getEnvironment().metrics(), "migrations");

        try (final Connection connection = ds.getConnection()) {
            final Liquibase migrator = new Liquibase(
                    "migrations.xml",
                    new ClassLoaderResourceAccessor(),
                    new JdbcConnection(connection)
            );
            migrator.update("");
        }

        JDBI = new JdbiFactory().build(RULE.getEnvironment(), RULE.getConfiguration().getDatabase(), "sql");
        IN_REPOSITORY = JDBI.onDemand(InRepository.class);
    }

    @After
    public void setup() {
        this.executeSeedSql("truncate.sql");
    }

    @Test
    public void testGetNonExistentPlant() {
        // Given

        // When
        final Optional<Plant> plantOptional = IN_REPOSITORY.getPlant(1);

        // Then
        Assert.assertFalse(plantOptional.isPresent());
    }

    @Test
    public void getExistentPlant() {
        // Given
        this.executeSeedSql("simple.sql");

        // When
        final Optional<Plant> plantOptional = IN_REPOSITORY.getPlant(1);

        // Then
        Assert.assertTrue(plantOptional.isPresent());
        final Plant plant = plantOptional.get();
        Assert.assertEquals("a", plant.getPassword());
        Assert.assertEquals("cactus", plant.getType());
    }

    private void executeSeedSql(final String filename) {
        final String script = FixtureHelpers.fixture(FIXTURES_ROOT + filename);
        try (final Handle h = JDBI.open()) {
            h.createScript(script).execute();
        }
    }

}
