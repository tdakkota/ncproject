package org.tdakkota.ncproject.resources;

import io.quarkus.panache.common.Page;
import org.jboss.resteasy.spi.NoLogWebApplicationException;
import org.tdakkota.ncproject.entities.Area;
import org.tdakkota.ncproject.repos.AreaRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/area")
public class AreaResource {
    @Inject
    AreaRepository repo;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Area get(@PathParam("id") Long id) {
        Area status = repo.findById(id);
        if (status == null) {
            throw new NoLogWebApplicationException(Response.Status.NOT_FOUND);
        }
        return status;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Area> list(@QueryParam("page") @DefaultValue("0") int pageIndex,
                           @QueryParam("size") @DefaultValue("20") int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        return repo.findAll().page(page).list();
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@Valid Area e) {
        repo.persist(e);
        return Response.status(Response.Status.CREATED).entity(e).build();
    }

    @Transactional
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, @Valid Area e) {
        Area result = repo.update(id, e);
        return Response.status(Response.Status.CREATED).entity(result).build();
    }

    @Transactional
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        if (!repo.deleteById(id)) {
            throw new NoLogWebApplicationException(404);
        }
    }
}
