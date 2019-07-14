package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.IntegrationTest;
import com.jsharpe.plantlive.PlantliveApplication;
import com.jsharpe.plantlive.PlantliveConfiguration;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.plants.out.PlantOutRepository;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.jdbi.v3.core.Jdbi;
import org.junit.*;
import org.junit.experimental.categories.Category;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Category(IntegrationTest.class)
public class SqlPlantOutRepositoryTest {

    @ClassRule
    public static final DropwizardAppRule<PlantliveConfiguration> RULE =
            new DropwizardAppRule<>(PlantliveApplication.class, ResourceHelpers.resourceFilePath("postgres-only.yml"));

    private static final String FIXTURES_ROOT = "fixtures/sql/";

    private static Jdbi JDBI;
    private static PlantOutRepository PLANT_OUT_REPOSITORY;

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
        PLANT_OUT_REPOSITORY = JDBI.onDemand(PlantOutRepository.class);
    }

    @After
    public void teardown() {
        SqlUtils.executeSeedSql(JDBI, FIXTURES_ROOT + "truncate.sql");
    }

    @Test
    public void testGetNonExistentPlant() {
        // Given
        final UUID someUserId = UUID.fromString("12344321-abcd-cdef-fedc-123412340000");

        // When
        final Optional<Plant> plantOptional = PLANT_OUT_REPOSITORY.getByUserId(someUserId);

        // Then
        Assert.assertFalse(plantOptional.isPresent());
    }

    @Test
    public void getExistentPlant() {
        // Given
        SqlUtils.executeSeedSql(JDBI, FIXTURES_ROOT + "simple.sql");

        // When
        final Optional<Plant> plantOptional = PLANT_OUT_REPOSITORY.getByUserId(UUID.fromString("a483df51-e18e-4053-9875-6c753ca14997"));

        // Then
        Assert.assertTrue(plantOptional.isPresent());
        final Plant plant = plantOptional.get();
        Assert.assertEquals("a", plant.getPassword());
        Assert.assertEquals("cactus", plant.getType());
    }

}
