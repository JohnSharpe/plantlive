package com.jsharpe.plantlive.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsharpe.plantlive.UnitTest;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.Date;

@Category(UnitTest.class)
public class SummarySerializationTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void testMinimal() throws IOException {
        // Given
        final Summary minimal = new Summary(0, 0, 0, 0, 0);
        final String expected = MAPPER.writeValueAsString(minimal);

        // When
        final String actual = MAPPER.writeValueAsString(
                MAPPER.readValue(FixtureHelpers.fixture("fixtures/summary/minimal.json"), Summary.class)
        );

        // Then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormal() throws IOException {
        // Given
        final Summary normal = new Summary(1565630997187L, 24, 73, 60, 4);
        final String expected = MAPPER.writeValueAsString(normal);

        // When
        final String actual = MAPPER.writeValueAsString(
                MAPPER.readValue(FixtureHelpers.fixture("fixtures/summary/normal.json"), Summary.class)
        );

        // Then
        Assert.assertEquals(expected, actual);
    }

}
