package org.tdakkota.ncproject.resources;

import org.tdakkota.ncproject.entities.Incident;
import org.tdakkota.ncproject.services.IncidentService;

import javax.inject.Inject;
import javax.ws.rs.*;

public class IncidentResource {
    @Inject
    IncidentService service;

    @GET
    @Path("{id}")
    public Incident get(@PathParam("id") Long id) {
        return service.get(id);
    }

    @POST
    public Incident add(Incident incidentToSave) {
        return service.add(incidentToSave);
    }

    @PUT
    @Path("{id}")
    public Incident update(Incident incidentToSave) {
        return service.update(incidentToSave);
    }

    @DELETE
    @Path("{id}")
    public void close(@PathParam("id") Long id) {
        service.close(id);
    }
}
