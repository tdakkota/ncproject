package org.tdakkota.ncproject.resources;

import org.tdakkota.ncproject.entities.Incident;
import org.tdakkota.ncproject.services.IncidentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/incident")
public class IncidentResource {
    @Inject
    IncidentService service;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Incident get(@PathParam("id") Long id) {
        return service.get(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Incident add(Incident incidentToSave) {
        return service.add(incidentToSave);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Incident update(Incident incidentToSave) {
        return service.update(incidentToSave);
    }

    @DELETE
    @Path("{id}")
    public void close(@PathParam("id") Long id) {
        service.close(id);
    }
}
