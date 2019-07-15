package com.jsharpe.plantlive;

import com.jsharpe.plantlive.api.NewPlant;
import com.jsharpe.plantlive.api.Summary;
import com.jsharpe.plantlive.repositories.SqlUtils;
import io.dropwizard.client.JerseyClientBuilder;
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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;

@Category(IntegrationTest.class)
public class CreatePlantTest {

    @ClassRule
    public static final DropwizardAppRule<PlantliveConfiguration> RULE =
            new DropwizardAppRule<>(PlantliveApplication.class, ResourceHelpers.resourceFilePath("postgres-http.yml"));

    private static Jdbi JDBI;
    private static Client CLIENT;

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
        CLIENT = new JerseyClientBuilder(RULE.getEnvironment()).build("test");
    }

    @AfterClass
    public static void teardownClass() {
        SqlUtils.executeSeedSql(JDBI, SqlUtils.FIXTURES_ROOT + "truncate.sql");
        CLIENT.close();
    }

    @Test
    public void testCreatePlant() {
        // Given
        final NewPlant newPlant = new NewPlant("master123", "end2end", "cactus");

        // When
        final Response response = CLIENT.target("http://localhost:" + RULE.getLocalPort())
                .path("plant")
                .request()
                .post(Entity.json(newPlant));

        // Then
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatus());

        final String userId = response.readEntity(String.class);

        // When
        final Response summaryResponse = CLIENT.target("http://localhost:" + RULE.getLocalPort())
                .queryParam("id", userId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatus());
        final Summary summary = summaryResponse.readEntity(Summary.class);
        Assert.assertEquals(0, summary.getTemperature(), 0.000001);
        Assert.assertEquals(0, summary.getHumidity(), 0.000001);
        Assert.assertEquals(0, summary.getLight(), 0.000001);
        Assert.assertEquals(0, summary.getConductivity(), 0.000001);
    }

}
