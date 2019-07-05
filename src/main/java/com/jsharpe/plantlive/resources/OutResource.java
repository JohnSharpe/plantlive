package com.jsharpe.plantlive.resources;

import com.jsharpe.plantlive.api.Summary;
import com.jsharpe.plantlive.models.Plant;
import com.jsharpe.plantlive.repositories.details.out.DetailOutRepository;
import com.jsharpe.plantlive.repositories.plants.out.PlantOutRepository;
import com.jsharpe.plantlive.resources.date.DateSupplier;
import com.jsharpe.plantlive.views.StandardView;
import com.jsharpe.plantlive.views.SummaryView;
import io.dropwizard.views.View;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.Optional;

@Path("/")
public class OutResource {

    private static final StandardView STANDARD_VIEW = new StandardView(false);
    private static final StandardView STANDARD_VIEW_NO_PLANT = new StandardView(true);

    private final PlantOutRepository plantOutRepository;
    private final DetailOutRepository detailOutRepository;
    private final DateSupplier dateSupplier;

    public OutResource(
            final PlantOutRepository plantOutRepository,
            final DetailOutRepository detailOutRepository,
            final DateSupplier dateSupplier
    ) {
        this.plantOutRepository = plantOutRepository;
        this.detailOutRepository = detailOutRepository;
        this.dateSupplier = dateSupplier;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Summary getSummaryJson(@QueryParam("id") Long id) {

        if (id == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        // TODO This variable name isn't great
        final Date yesterday = this.dateSupplier.getDate();
        return this.detailOutRepository.getSummary(id, yesterday);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public View getSummaryHtml(@QueryParam("id") Long id) {

        if (id == null) {
            return STANDARD_VIEW;
        } else {
            final Optional<Plant> plantOptional = this.plantOutRepository.get(id);

            if (plantOptional.isPresent()) {

                // TODO Use plant type to get some parameters.
                final Plant plant = plantOptional.get();
                final Date since = this.dateSupplier.getDate();
                final Summary summary = this.detailOutRepository.getSummary(id, since);
                return new SummaryView(summary);

            } else {
                return STANDARD_VIEW_NO_PLANT;
            }

        }

    }

}
