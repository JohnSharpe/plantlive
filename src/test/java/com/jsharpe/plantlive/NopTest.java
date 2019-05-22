package com.jsharpe.plantlive;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

@Category(IntegrationTest.class)
public class NopTest {

    @ClassRule
    public static final DropwizardAppRule<PlantliveConfiguration> RULE =
            new DropwizardAppRule<>(
                    PlantliveApplication.class,
                    ResourceHelpers.resourceFilePath("nop.yml")
            );

    @Test
    public void testNopConfiguration() {
        final Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");

        final Response response = client.target(
                String.format("http://localhost:%d/ping", RULE.getAdminPort()))
                .request()
                .get();

        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("pong\n", response.readEntity(String.class));
    }

}
