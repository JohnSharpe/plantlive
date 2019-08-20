package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.IntegrationTest;
import com.jsharpe.plantlive.PlantliveApplication;
import com.jsharpe.plantlive.PlantliveConfiguration;
import com.jsharpe.plantlive.repositories.details.in.DetailInRepository;
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
import java.util.Date;

@Category(IntegrationTest.class)
public class SqlDetailInRepositoryTest {

    @ClassRule
    public static final DropwizardAppRule<PlantliveConfiguration> RULE =
            new DropwizardAppRule<>(PlantliveApplication.class, ResourceHelpers.resourceFilePath("postgres-only.yml"));

    private static Jdbi JDBI;
    private static DetailInRepository DETAIL_IN_REPOSITORY;

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
        DETAIL_IN_REPOSITORY = JDBI.onDemand(DetailInRepository.class);
    }

    @After
    public void teardown() {
        SqlUtils.executeSeedSql(JDBI, SqlUtils.FIXTURES_ROOT + "truncate.sql");
    }

    @Test
    public void writeDetail() {
        // Given
        SqlUtils.executeSeedSql(JDBI, SqlUtils.FIXTURES_ROOT + "simple.sql");

        // When
        final int rows = DETAIL_IN_REPOSITORY.save(3, new Date(), 20, 89, 32, 12);

        // Then
        Assert.assertEquals(1, rows);
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void writeDetailToNonExistentPlant() {
        // Given
        SqlUtils.executeSeedSql(JDBI, SqlUtils.FIXTURES_ROOT + "simple.sql");

        // When
        DETAIL_IN_REPOSITORY.save(505, new Date(), 20, 89, 32, 12);

        // Then (exception)
    }

}
