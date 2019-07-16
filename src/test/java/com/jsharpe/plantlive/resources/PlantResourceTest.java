package com.jsharpe.plantlive.resources;

import com.jsharpe.plantlive.UnitTest;
import com.jsharpe.plantlive.api.NewPlant;
import com.jsharpe.plantlive.config.masterPassword.DirectMasterPasswordCheck;
import com.jsharpe.plantlive.config.masterPassword.MasterPasswordCheck;
import com.jsharpe.plantlive.consume.PasswordHasher;
import com.jsharpe.plantlive.exceptions.IllegalPasswordException;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.MockRepository;
import com.jsharpe.plantlive.repositories.plants.in.PlantInRepository;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;
import java.util.Set;

@Category(UnitTest.class)
public class PlantResourceTest {

    private final MockRepository mockRepository;
    private final PlantResource plantResource;

    public PlantResourceTest() {
        this.mockRepository = new MockRepository();
        this.plantResource = new PlantResource(
                new DirectMasterPasswordCheck("master"),
                this.mockRepository.getPlantInRepository()
        );
    }

    @Test
    public void testAddPlantWithBadMasterPassword() {
        // Given
        final NewPlant newPlant = new NewPlant("slave", "whatever", "cactus");

        // When
        final Response response = this.plantResource.addPlant(newPlant);

        // Then
        Assert.assertNotNull(response);
        Assert.assertEquals(403, response.getStatus());
    }

    @Test
    public void testAddPlantWithNullPassword() {
        // Given
        final NewPlant newPlant = new NewPlant("master", null, "cactus");

        // When
        final Response response = this.plantResource.addPlant(newPlant);

        // Then
        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getStatus());
    }

    @Test
    public void testAddPlantWithEmptyPassword() {
        // Given
        final NewPlant newPlant = new NewPlant("master", "", "cactus");

        // When
        final Response response = this.plantResource.addPlant(newPlant);

        // Then
        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getStatus());
    }

    @Test
    public void testAddPlant() throws IllegalPasswordException {
        // Given
        final String password = "happy";
        final String hashedPassword = PasswordHasher.hash(password);
        final NewPlant newPlant = new NewPlant("master", password, "cactus");

        // When
        final Response response = this.plantResource.addPlant(newPlant);

        // Then
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatus());

        final Set<Plant> plants = this.mockRepository.getPlants();
        Assert.assertEquals(1, plants.size());
        for (final Plant plant : plants) {
            Assert.assertEquals(hashedPassword, plant.getPassword());
            Assert.assertEquals("cactus", plant.getType());
        }
    }

    @Test
    public void testAddPlantToNaughtyDatabase() {
        // Given
        final MasterPasswordCheck masterPasswordCheck = new DirectMasterPasswordCheck("master");
        final PlantInRepository plantInRepository = Mockito.mock(PlantInRepository.class);
        //noinspection unchecked
        Mockito.when(
                plantInRepository.save(
                        Mockito.any(),
                        Mockito.anyString(),
                        Mockito.anyString()
                )
        ).thenThrow(UnableToExecuteStatementException.class);

        final PlantResource plantResource = new PlantResource(masterPasswordCheck, plantInRepository);
        final NewPlant newPlant = new NewPlant("master", "sad", "cactus");

        // When
        final Response response = plantResource.addPlant(newPlant);

        // Then
        Assert.assertNotNull(response);
        Assert.assertEquals(500, response.getStatus());
    }

    @Test
    public void testAddPlantToDudDatabase() {
        // Given
        final MasterPasswordCheck masterPasswordCheck = new DirectMasterPasswordCheck("master");
        final PlantInRepository plantInRepository = Mockito.mock(PlantInRepository.class);
        Mockito.when(
                plantInRepository.save(
                        Mockito.any(),
                        Mockito.anyString(),
                        Mockito.anyString()
                )
        ).thenReturn(0);

        final PlantResource plantResource = new PlantResource(masterPasswordCheck, plantInRepository);
        final NewPlant newPlant = new NewPlant("master", "sad", "cactus");

        // When
        final Response response = plantResource.addPlant(newPlant);

        // Then
        Assert.assertNotNull(response);
        Assert.assertEquals(500, response.getStatus());
    }

    @Test
    public void testAddPlantToInsaneDatabase() {
        // Given
        final MasterPasswordCheck masterPasswordCheck = new DirectMasterPasswordCheck("master");
        final PlantInRepository plantInRepository = Mockito.mock(PlantInRepository.class);
        Mockito.when(
                plantInRepository.save(
                        Mockito.any(),
                        Mockito.anyString(),
                        Mockito.anyString()
                )
        ).thenReturn(2);

        final PlantResource plantResource = new PlantResource(masterPasswordCheck, plantInRepository);
        final NewPlant newPlant = new NewPlant("master", "sad", "cactus");

        // When
        final Response response = plantResource.addPlant(newPlant);

        // Then
        Assert.assertNotNull(response);
        Assert.assertEquals(500, response.getStatus());
    }

}
