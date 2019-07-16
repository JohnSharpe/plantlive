package com.jsharpe.plantlive.resources;

import com.jsharpe.plantlive.api.NewPlant;
import com.jsharpe.plantlive.config.masterPassword.MasterPasswordCheck;
import com.jsharpe.plantlive.consume.PasswordHasher;
import com.jsharpe.plantlive.exceptions.IllegalPasswordException;
import com.jsharpe.plantlive.repositories.plants.in.PlantInRepository;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/plant")
public class PlantResource {

    private final MasterPasswordCheck masterPasswordCheck;
    private final PlantInRepository plantInRepository;

    public PlantResource(
            final MasterPasswordCheck masterPasswordCheck,
            final PlantInRepository plantInRepository
    ) {
        this.masterPasswordCheck = masterPasswordCheck;
        this.plantInRepository = plantInRepository;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPlant(final NewPlant newPlant) {

        if (this.masterPasswordCheck.matches(newPlant.getMasterPassword())) {

            final UUID userId = UUID.randomUUID();

            final String hashedPassword;
            try {
                hashedPassword = PasswordHasher.hash(newPlant.getPassword());
            } catch (IllegalPasswordException e) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Could not hash password").build();
            }

            // TODO This might end up being an enum
            final String type = newPlant.getType();

            final int rowsAdded;
            try {
                rowsAdded = this.plantInRepository.save(userId, hashedPassword, type);
            } catch (UnableToExecuteStatementException e) {
                // The uuid may have clashed?
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unable to execute INSERT:\n" + e.getMessage()).build();
            }

            if (rowsAdded < 1) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Insert failed!").build();
            } else if (rowsAdded > 1) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Multiple rows added!!").build();
            } else {
                return Response.ok().entity(userId.toString()).build();
            }

        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Incorrect password").build();
        }

    }

}
