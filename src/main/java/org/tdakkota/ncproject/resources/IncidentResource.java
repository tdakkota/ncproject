package org.tdakkota.ncproject.resources;

import io.quarkus.panache.common.Page;
import org.tdakkota.ncproject.entities.AddIncidentRequest;
import org.tdakkota.ncproject.entities.Incident;
import org.tdakkota.ncproject.services.IncidentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Incident> list(@QueryParam("page") @DefaultValue("0") int pageIndex,
                               @QueryParam("size") @DefaultValue("20") int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        return service.list(page);
    }

    @GET
    @Path("/area/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Incident> getByArea(
            @PathParam("id") Long id,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize
    ) {
        return service.getIncidentsByArea(id, Page.of(pageIndex, pageSize));
    }

    @GET
    @Path("/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Incident> getByUser(
            @PathParam("id") Long id,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize
    ) {
        return service.getIncidentsByUser(id, Page.of(pageIndex, pageSize));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(AddIncidentRequest incidentToSave) {
        return Response.status(Response.Status.CREATED).
                entity(service.add(incidentToSave)).
                build();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, AddIncidentRequest incidentToSave) {
        return Response.status(Response.Status.CREATED).
                entity(service.update(id, incidentToSave)).
                build();
    }

    @DELETE
    @Path("{id}")
    public void close(@PathParam("id") Long id) {
        service.close(id);
    }
}
