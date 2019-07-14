package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.IntegrationTest;
import com.jsharpe.plantlive.PlantliveApplication;
import com.jsharpe.plantlive.PlantliveConfiguration;
import com.jsharpe.plantlive.consume.PasswordHasher;
import com.jsharpe.plantlive.exceptions.IllegalPasswordException;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.plants.in.PlantInRepository;
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
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.junit.*;
import org.junit.experimental.categories.Category;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Category(IntegrationTest.class)
public class SqlPlantInRepositoryTest {

    @ClassRule
    public static final DropwizardAppRule<PlantliveConfiguration> RULE =
            new DropwizardAppRule<>(PlantliveApplication.class, ResourceHelpers.resourceFilePath("postgres-only.yml"));

    private static final String FIXTURES_ROOT = "fixtures/sql/";

    private static Jdbi JDBI;
    private static PlantInRepository PLANT_IN_REPOSITORY;

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
        PLANT_IN_REPOSITORY = JDBI.onDemand(PlantInRepository.class);
    }

    @After
    public void teardown() {
        SqlUtils.executeSeedSql(JDBI, FIXTURES_ROOT + "truncate.sql");
    }

    @Test
    public void testNormalAdd() throws IllegalPasswordException {
        // Given

        // When
        final int rows = PLANT_IN_REPOSITORY.save(UUID.randomUUID(), PasswordHasher.hash("whatever"), "cactus");

        // Then
        Assert.assertEquals(1, rows);
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testUserIdUniqueness() throws IllegalPasswordException {
        // Given
        final UUID userId = UUID.randomUUID();

        // When
        final int rows = PLANT_IN_REPOSITORY.save(userId, PasswordHasher.hash("whatever"), "cactus");
        Assert.assertEquals(1, rows);

        PLANT_IN_REPOSITORY.save(userId, PasswordHasher.hash("else"), "tulip");
        // Then (exception)

    }

}
