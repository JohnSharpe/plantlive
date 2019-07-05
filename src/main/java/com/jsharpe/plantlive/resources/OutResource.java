package com.jsharpe.plantlive.resources;

import com.jsharpe.plantlive.api.Summary;
import com.jsharpe.plantlive.resources.date.DateSupplier;
import com.jsharpe.plantlive.repositories.out.OutRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.Optional;

public class OutResource {

    private final OutRepository outRepository;
    private final DateSupplier dateSupplier;

    public OutResource(
            final OutRepository outRepository,
            final DateSupplier dateSupplier
    ) {
        this.outRepository = outRepository;
        this.dateSupplier = dateSupplier;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Summary getSummaryJson(@PathParam("id") long id) {

        // TODO This variable name isn't great
        final Date yesterday = this.dateSupplier.getDate();
        final Optional<Summary> summaryOptional = this.outRepository.getSummary(id, yesterday);

        if (summaryOptional.isPresent()) {
            return summaryOptional.get();
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

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
