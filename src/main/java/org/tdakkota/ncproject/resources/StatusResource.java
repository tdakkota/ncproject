package org.tdakkota.ncproject.resources;

import org.tdakkota.ncproject.entities.Status;
import org.tdakkota.ncproject.entities.StatusBody;
import org.tdakkota.ncproject.services.StatusService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/status")
public class StatusResource {
    @Inject
    StatusService service;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Status get(@PathParam("id") Long id) {
        return service.get(id).orElseThrow(NotFoundException::new);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Status> list(@QueryParam("page") @DefaultValue("0") int pageIndex,
                             @QueryParam("size") @DefaultValue("20") int pageSize) {
        return service.list(pageIndex, pageSize);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@Valid StatusBody e) {
        return Response.status(Response.Status.CREATED).entity(service.add(e)).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, @Valid StatusBody e) {
        return service.update(id, e).map(Response::ok).orElseThrow(NotFoundException::new).build();
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        if (!service.delete(id)) {
            throw new NotFoundException();
        }
    }
}
