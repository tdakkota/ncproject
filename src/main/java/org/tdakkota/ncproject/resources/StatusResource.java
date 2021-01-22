package org.tdakkota.ncproject.resources;

import io.quarkus.panache.common.Page;
import org.tdakkota.ncproject.entities.Status;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/status")
public class StatusResource {
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Status get(@PathParam("id") Long id) {
        Status status = Status.findById(id);
        if (status == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return status;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Status> list(@QueryParam("page") @DefaultValue("0") int pageIndex,
                             @QueryParam("size") @DefaultValue("20") int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        return Status.findAll().page(page).list();
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@Valid Status e) {
        e.persist();
        return Response.status(Response.Status.CREATED).entity(e).build();
    }

    @Transactional
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, @Valid Status e) {
        Status exist = Status.findById(id);
        if (exist == null) {
            e.persist();
            return Response.status(Response.Status.CREATED).entity(e).build();
        }

        Status result = exist.update(e);
        return Response.status(Response.Status.CREATED).entity(exist).build();
    }

    @Transactional
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        if (!Status.deleteById(id)) {
            throw new WebApplicationException(404);
        }
    }
}
