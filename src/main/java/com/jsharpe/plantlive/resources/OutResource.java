package com.jsharpe.plantlive.resources;

import com.jsharpe.plantlive.api.Summary;
import com.jsharpe.plantlive.repositories.details.out.DetailOutRepository;
import com.jsharpe.plantlive.repositories.plants.out.PlantOutRepository;
import com.jsharpe.plantlive.resources.date.DateSupplier;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;

public class OutResource {

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
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Summary getSummaryJson(@PathParam("id") long id) {

        // TODO This variable name isn't great
        final Date yesterday = this.dateSupplier.getDate();
        return this.detailOutRepository.getSummary(id, yesterday);

    }

//    @GET
//    @Path("{id}")
//    @Produces(MediaType.TEXT_HTML)
//    public View getSummaryView(@PathParam("id") long id) {
//        // Render a view with outRepository.getSummary(id, yesterday);
//    }
//
//    @GET
//    @Produces(MediaType.TEXT_HTML)
//    public View getDefaultView() {
//        // Render a basic view, probably with a single-input form which hits the above.
//    }

}
