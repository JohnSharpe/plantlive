package com.jsharpe.plantlive.resources;

import com.jsharpe.plantlive.IntegrationTest;
import com.jsharpe.plantlive.PlantliveApplication;
import com.jsharpe.plantlive.PlantliveConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;
import org.junit.experimental.categories.Category;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Category(IntegrationTest.class)
public class HttpTest {

    @ClassRule
    public static final DropwizardAppRule<PlantliveConfiguration> RULE =
            new DropwizardAppRule<>(PlantliveApplication.class, ResourceHelpers.resourceFilePath("http-only.yml"));

    private static Client CLIENT;

    @BeforeClass
    public static void setupClass() {
        CLIENT = new JerseyClientBuilder(RULE.getEnvironment()).build("test");
    }

    @AfterClass
    public static void teardownClass() {
        CLIENT.close();
    }

    @Test
    public void testStandardHttpRequest() {
        // Given

        // When
        final Response response = CLIENT.target("http://localhost:" + RULE.getLocalPort())
                .request(MediaType.TEXT_HTML)
                .get();

        // Then
        Assert.assertEquals(200, response.getStatus());
        Assert.assertTrue(MediaType.TEXT_HTML_TYPE.isCompatible(response.getMediaType()));
        final String body = response.readEntity(String.class);
        Assert.assertTrue(body.contains("<!-- Standard -->"));
    }

    @Test
    public void testNoPlantHttpRequest() {
        // Given
        final String userId = "a483df51-e18e-4053-9875-6c753ca11111";

        // When
        final Response response = CLIENT.target("http://localhost:" + RULE.getLocalPort())
                .queryParam("id", userId)
                .request(MediaType.TEXT_HTML)
                .get();

        // Then
        Assert.assertEquals(200, response.getStatus());
        Assert.assertTrue(MediaType.TEXT_HTML_TYPE.isCompatible(response.getMediaType()));
        final String body = response.readEntity(String.class);
        Assert.assertTrue(body.contains("<!-- Standard -->"));
        Assert.assertTrue(body.contains("<!-- No plant -->"));
    }

    @Test
    public void testNoPlantJsonRequest() {
        // Given
        final String userId = "a483df51-e18e-4053-9875-6c753ca11111";

        // When
        final Response response = CLIENT.target("http://localhost:" + RULE.getLocalPort())
                .queryParam("id", userId)
                .request(MediaType.APPLICATION_JSON)
                .get();

        // Then
        Assert.assertEquals(404, response.getStatus());
        Assert.assertTrue(MediaType.APPLICATION_JSON_TYPE.isCompatible(response.getMediaType()));
    }

}
