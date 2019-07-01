package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.IntegrationTest;
import com.jsharpe.plantlive.PlantliveApplication;
import com.jsharpe.plantlive.PlantliveConfiguration;
import com.jsharpe.plantlive.api.Summary;
import com.jsharpe.plantlive.repositories.out.OutRepository;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Category(IntegrationTest.class)
public class SqlOutRepositoryTest {

    @ClassRule
    public static final DropwizardAppRule<PlantliveConfiguration> RULE =
            new DropwizardAppRule<>(PlantliveApplication.class, ResourceHelpers.resourceFilePath("postgres-only.yml"));

    private static final String FIXTURES_ROOT = "fixtures/sql/";

    private static Jdbi JDBI;
    private static OutRepository OUT_REPOSITORY;

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
        OUT_REPOSITORY = JDBI.onDemand(OutRepository.class);
    }

    @After
    public void teardown() {
        SqlUtils.executeSeedSql(JDBI, FIXTURES_ROOT + "truncate.sql");
    }

    @Test
    public void testGetSummaryWithAllValues() {
        // Given
        SqlUtils.executeSeedSql(JDBI, FIXTURES_ROOT + "simple.sql");
        final long id = 2;
        final Date since;
        {
            final Calendar sinceCal = Calendar.getInstance();
            sinceCal.set(2018, Calendar.NOVEMBER, 21, 8, 59, 0);
            since = sinceCal.getTime();
        }

        // When
        final Optional<Summary> summaryOptional = OUT_REPOSITORY.getSummary(id, since);

        // Then
        Assert.assertTrue(summaryOptional.isPresent());
        final Summary summary = summaryOptional.get();

//        Assert.assertEquals(2, summary.getId());
//        Assert.assertEquals("rose", summary.getType());
        Assert.assertEquals(22.0, summary.getTemperature(), 0.0000000001);
        Assert.assertEquals(83.2, summary.getHumidity(), 0.0000000001);
        Assert.assertEquals(43.8, summary.getLight(), 0.0000000001);
        Assert.assertEquals(21.1, summary.getConductivity(), 0.0000000001);
    }

    @Test
    public void testGetSummaryWithSomeValues() {
        // Given
        SqlUtils.executeSeedSql(JDBI, FIXTURES_ROOT + "simple.sql");
        final long id = 2;
        final Date since;
        {
            final Calendar sinceCal = Calendar.getInstance();
            sinceCal.set(2018, Calendar.NOVEMBER, 21, 13, 30, 0);
            since = sinceCal.getTime();
        }

        // When
        final Optional<Summary> summaryOptional = OUT_REPOSITORY.getSummary(id, since);

        // Then
        Assert.assertTrue(summaryOptional.isPresent());
        final Summary summary = summaryOptional.get();

//        Assert.assertEquals(2, summary.getId());
//        Assert.assertEquals("rose", summary.getType());
        Assert.assertEquals(23.4, summary.getTemperature(), 0.0000000001);
        Assert.assertEquals(81.4, summary.getHumidity(), 0.0000000001);
        Assert.assertEquals(43.8, summary.getLight(), 0.0000000001);
        Assert.assertEquals(22.0, summary.getConductivity(), 0.0000000001);
    }

    @Test
    public void testGetSummaryWithoutValues() {
        // Given
        SqlUtils.executeSeedSql(JDBI, FIXTURES_ROOT + "simple.sql");
        final long id = 3;
        final Date since;
        {
            final Calendar sinceCal = Calendar.getInstance();
            sinceCal.set(2018, Calendar.NOVEMBER, 21, 8, 59, 0);
            since = sinceCal.getTime();
        }

        // When
        final Optional<Summary> summaryOptional = OUT_REPOSITORY.getSummary(id, since);

        // Then
        Assert.assertTrue(summaryOptional.isPresent());
        final Summary summary = summaryOptional.get();

//        Assert.assertEquals(3, summary.getId());
//        Assert.assertEquals("dandelion", summary.getType());
        Assert.assertEquals(0, summary.getTemperature(), 0.0000000001);
        Assert.assertEquals(0, summary.getHumidity(), 0.0000000001);
        Assert.assertEquals(0, summary.getLight(), 0.0000000001);
        Assert.assertEquals(0, summary.getConductivity(), 0.0000000001);
    }

}
