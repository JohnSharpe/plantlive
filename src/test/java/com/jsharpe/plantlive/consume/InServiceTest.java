package com.jsharpe.plantlive.consume;

import com.jsharpe.plantlive.UnitTest;
import com.jsharpe.plantlive.exceptions.ConsumeException;
import com.jsharpe.plantlive.exceptions.IllegalPasswordException;
import com.jsharpe.plantlive.models.Detail;
import com.jsharpe.plantlive.repositories.MockDetailRepository;
import com.jsharpe.plantlive.repositories.MockPlantRepository;
import com.jsharpe.plantlive.repositories.detail.NopDetailRepository;
import com.jsharpe.plantlive.repositories.plant.NopPlantRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;
import java.util.Set;

@Category(UnitTest.class)
public class InServiceTest {

    @Test(expected = ConsumeException.class)
    public void testNoSuchPlant() throws ConsumeException {
        // Given
        final InService inService = new InService(
                new NopPlantRepository(),
                new NopDetailRepository(),
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
        final InService inService = new InService(
                new MockPlantRepository("super-secret", "cactus", "test"),
                new NopDetailRepository(),
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

        final InService inService = new InService(
                new MockPlantRepository(hashed, "cactus", "test"),
                new NopDetailRepository(),
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
        final MockDetailRepository mockDetailRepository = new MockDetailRepository();

        final InService inService = new InService(
                new MockPlantRepository(hashed, "tulip", "test"),
                mockDetailRepository,
                24
        );

        // When
        inService.write(1, password, timestamp, 23, 97, 44, 32);

        // Then
        final Set<Detail> savedDetails = mockDetailRepository.get();
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
