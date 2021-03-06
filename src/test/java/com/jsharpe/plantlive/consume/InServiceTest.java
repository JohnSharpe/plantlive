package com.jsharpe.plantlive.consume;

import com.jsharpe.plantlive.UnitTest;
import com.jsharpe.plantlive.exceptions.ConsumeException;
import com.jsharpe.plantlive.exceptions.IllegalPasswordException;
import com.jsharpe.plantlive.models.Detail;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.MockRepository;
import com.jsharpe.plantlive.repositories.details.in.NopDetailInRepository;
import com.jsharpe.plantlive.repositories.plants.in.NopPlantInRepository;
import com.jsharpe.plantlive.repositories.plants.out.NopPlantOutRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Category(UnitTest.class)
public class InServiceTest {

    @Test(expected = ConsumeException.class)
    public void testNoSuchPlant() throws ConsumeException {
        // Given
        final InService inService = new InService(
                new NopPlantOutRepository(),
                new NopPlantInRepository(),
                new NopDetailInRepository()
        );

        // When
        inService.write(
                UUID.randomUUID(),
                "whatever",
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
        final UUID userId = UUID.randomUUID();
        final Set<Plant> givenPlants = new HashSet<>();
        givenPlants.add(new Plant(-1, userId, "super-secret", "cactus"));
        final MockRepository mockRepository = new MockRepository();
        mockRepository.populate(givenPlants, null);

        final InService inService = new InService(
                mockRepository.getPlantOutRepository(),
                mockRepository.getPlantInRepository(),
                mockRepository.getDetailInRepository()
        );

        // When
        inService.write(
                userId,
                "cactus",
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
        final UUID userId = UUID.randomUUID();
        final String password = "diet cola";
        final String hashed = PasswordHasher.hash(password);

        final Set<Plant> givenPlants = new HashSet<>();
        givenPlants.add(new Plant(-1, userId, hashed, "cactus"));
        final MockRepository mockRepository = new MockRepository();
        mockRepository.populate(givenPlants, null);

        final InService inService = new InService(
                mockRepository.getPlantOutRepository(),
                mockRepository.getPlantInRepository(),
                mockRepository.getDetailInRepository()
        );

        // When
        inService.write(
                userId,
                password,
                "cactus",
                new Date(),
                -300.0,
                20.0,
                30.0,
                40.0
        );
    }

    @Test
    public void testValidSave() throws IllegalPasswordException, ConsumeException {
        // Given
        final UUID userId = UUID.randomUUID();
        final String type = "tulip";
        final String password = "headphones";
        final String hashed = PasswordHasher.hash(password);
        final Date timestamp = new Date();

        final Set<Plant> givenPlants = new HashSet<>();
        givenPlants.add(new Plant(-1, userId, hashed, type));
        final MockRepository mockRepository = new MockRepository();
        mockRepository.populate(givenPlants, null);

        final InService inService = new InService(
                mockRepository.getPlantOutRepository(),
                mockRepository.getPlantInRepository(),
                mockRepository.getDetailInRepository()
        );

        // When
        inService.write(userId, type, password, timestamp, 23, 97, 44, 32);

        // Then
        final Set<Plant> savedPlants = mockRepository.getPlants();
        Assert.assertEquals(1, savedPlants.size());

        final Plant plant = savedPlants.stream().findFirst().orElseThrow(RuntimeException::new);
        Assert.assertEquals(type, plant.getType());

        final Set<Detail> savedDetails = mockRepository.getDetails();
        Assert.assertEquals(1, savedDetails.size());

        final Detail detail = savedDetails.stream().findFirst().orElseThrow(RuntimeException::new);

        // It's not our business which id is assigned.
        // But we'll throw this line in for coverage.
        Assert.assertEquals(1, detail.getId());

        // It's not our business which id is assigned.
        // BUT it is our business that it references the plant
        Assert.assertEquals(plant.getId(), detail.getPlantId());

        Assert.assertEquals(timestamp, detail.getInTimestamp());
        Assert.assertEquals(23, detail.getTemperature(), 0.0000001);
        Assert.assertEquals(97, detail.getHumidity(), 0.0000001);
        Assert.assertEquals(44, detail.getLight(), 0.0000001);
        Assert.assertEquals(32, detail.getConductivity(), 0.0000001);
    }

    @Test
    public void testChangeType() throws ConsumeException, IllegalPasswordException {
        // Given
        final UUID userId = UUID.randomUUID();
        final String type = "tulip";
        final String newType = "chrysanthemum";
        final String password = "headphones";
        final String hashed = PasswordHasher.hash(password);
        final Date timestamp = new Date();

        final Set<Plant> givenPlants = new HashSet<>();
        givenPlants.add(new Plant(-1, userId, hashed, type));
        final MockRepository mockRepository = new MockRepository();
        mockRepository.populate(givenPlants, null);

        final InService inService = new InService(
                mockRepository.getPlantOutRepository(),
                mockRepository.getPlantInRepository(),
                mockRepository.getDetailInRepository()
        );

        // When
        inService.write(userId, newType, password, timestamp, 23, 97, 44, 32);

        // Then
        final Set<Plant> savedPlants = mockRepository.getPlants();
        Assert.assertEquals(1, savedPlants.size());

        final Plant plant = savedPlants.stream().findFirst().orElseThrow(RuntimeException::new);
        Assert.assertEquals(newType, plant.getType());

        final Set<Detail> savedDetails = mockRepository.getDetails();
        Assert.assertEquals(1, savedDetails.size());

        final Detail detail = savedDetails.stream().findFirst().orElseThrow(RuntimeException::new);
        Assert.assertEquals(plant.getId(), detail.getPlantId());
        Assert.assertEquals(timestamp, detail.getInTimestamp());
        Assert.assertEquals(23, detail.getTemperature(), 0.0000001);
        Assert.assertEquals(97, detail.getHumidity(), 0.0000001);
        Assert.assertEquals(44, detail.getLight(), 0.0000001);
        Assert.assertEquals(32, detail.getConductivity(), 0.0000001);
    }

    // Assert the update goes through fine
    @Test
    public void testChangeTypeInvalid() throws ConsumeException, IllegalPasswordException {
        // Given
        final UUID userId = UUID.randomUUID();
        final String type = "tulip";
        final String password = "headphones";
        final String hashed = PasswordHasher.hash(password);
        final Date timestamp = new Date();

        final Set<Plant> givenPlants = new HashSet<>();
        givenPlants.add(new Plant(-1, userId, hashed, type));
        final MockRepository mockRepository = new MockRepository();
        mockRepository.populate(givenPlants, null);

        final InService inService = new InService(
                mockRepository.getPlantOutRepository(),
                mockRepository.getPlantInRepository(),
                mockRepository.getDetailInRepository()
        );

        // When
        inService.write(userId, "", password, timestamp, 23, 97, 44, 32);

        // Then
        final Set<Plant> savedPlants = mockRepository.getPlants();
        Assert.assertEquals(1, savedPlants.size());

        final Plant plant = savedPlants.stream().findFirst().orElseThrow(RuntimeException::new);
        // Not changed!
        Assert.assertEquals(type, plant.getType());

        final Set<Detail> savedDetails = mockRepository.getDetails();
        Assert.assertEquals(1, savedDetails.size());

        final Detail detail = savedDetails.stream().findFirst().orElseThrow(RuntimeException::new);
        Assert.assertEquals(plant.getId(), detail.getPlantId());
        Assert.assertEquals(timestamp, detail.getInTimestamp());
        Assert.assertEquals(23, detail.getTemperature(), 0.0000001);
        Assert.assertEquals(97, detail.getHumidity(), 0.0000001);
        Assert.assertEquals(44, detail.getLight(), 0.0000001);
        Assert.assertEquals(32, detail.getConductivity(), 0.0000001);
    }

}
