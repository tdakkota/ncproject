package org.tdakkota.ncproject.resources;

import org.jboss.resteasy.spi.NoLogWebApplicationException;
import org.tdakkota.ncproject.api.AddIncidentRequest;
import org.tdakkota.ncproject.api.IncidentFilter;
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
        return service.get(id).orElseThrow(() -> new NoLogWebApplicationException(Response.Status.NOT_FOUND));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Incident> list(@QueryParam("page") @DefaultValue("0") int pageIndex,
                               @QueryParam("size") @DefaultValue("20") int pageSize) {
        return service.list(pageIndex, pageSize);
    }

    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Incident> find(
            @PathParam("id") Long id,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @BeanParam IncidentFilter filter
    ) {
        return service.find(pageIndex, pageSize, filter);
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
    public Response update(@PathParam("id") Long id, AddIncidentRequest e) {
        return service.update(id, e).map(Response::ok).orElse(Response.status(Response.Status.NOT_FOUND)).build();
    }

    @DELETE
    @Path("{id}")
    public void close(@PathParam("id") Long id) {
        if (!service.close(id)) {
            throw new NoLogWebApplicationException(Response.Status.NOT_FOUND);
        }
    }
}
