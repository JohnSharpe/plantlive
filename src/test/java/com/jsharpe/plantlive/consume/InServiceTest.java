package com.jsharpe.plantlive.consume;

import com.jsharpe.plantlive.UnitTest;
import com.jsharpe.plantlive.exceptions.ConsumeException;
import com.jsharpe.plantlive.exceptions.IllegalPasswordException;
import com.jsharpe.plantlive.models.Detail;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.MockInRepository;
import com.jsharpe.plantlive.repositories.in.NopInRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Category(UnitTest.class)
public class InServiceTest {

    @Test(expected = ConsumeException.class)
    public void testNoSuchPlant() throws ConsumeException {
        // Given
        final InService inService = new InService(
                new NopInRepository(),
                24
        );

        // When
        inService.write(
                1,
                "whatever",
                new Date(),
                10,
                20,
                30,
                40
        );
    }

    @Test(expected = ConsumeException.class)
    public void testIncorrectPassword() throws ConsumeException {
        // Given
        final Set<Plant> givenPlants = new HashSet<>();
        givenPlants.add(new Plant(-1, "super-secret", "cactus"));
        final MockInRepository mockInRepository = new MockInRepository();
        mockInRepository.populate(givenPlants, null);

        final InService inService = new InService(
                mockInRepository,
                24
        );

        // When
        inService.write(
                1,
                "hacker",
                new Date(),
                10,
                20,
                30,
                40
        );
    }

    @Test(expected = ConsumeException.class)
    public void testInvalidTemperature() throws ConsumeException, IllegalPasswordException {
        // Given
        final String password = "diet cola";
        final String hashed = PasswordHasher.hash(password);

        final Set<Plant> givenPlants = new HashSet<>();
        givenPlants.add(new Plant(-1, hashed, "cactus"));
        final MockInRepository mockInRepository = new MockInRepository();
        mockInRepository.populate(givenPlants, null);

        final InService inService = new InService(
                mockInRepository,
                24
        );

        // When
        inService.write(
                1,
                password,
                new Date(),
                -300,
                20,
                30,
                40
        );
    }

    @Test
    public void testValidSave() throws IllegalPasswordException, ConsumeException {
        // Given
        final String password = "headphones";
        final String hashed = PasswordHasher.hash(password);
        final Date timestamp = new Date();

        final Set<Plant> givenPlants = new HashSet<>();
        givenPlants.add(new Plant(-1, hashed, "tulip"));
        final MockInRepository mockInRepository = new MockInRepository();
        mockInRepository.populate(givenPlants, null);

        final InService inService = new InService(
                mockInRepository,
                24
        );

        // When
        inService.write(1, password, timestamp, 23, 97, 44, 32);

        // Then
        final Set<Detail> savedDetails = mockInRepository.getDetails();
        Assert.assertEquals(1, savedDetails.size());

        final Detail detail = savedDetails.stream().findFirst().orElseThrow(RuntimeException::new);
        Assert.assertEquals(1, detail.getPlantId());
        Assert.assertEquals(timestamp, detail.getTimestamp());
        Assert.assertEquals(23, detail.getTemperature());
        Assert.assertEquals(97, detail.getHumidity());
        Assert.assertEquals(44, detail.getLight());
        Assert.assertEquals(32, detail.getConductivity());
    }

}
