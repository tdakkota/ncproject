package org.tdakkota.ncproject.resources;

import io.quarkus.panache.common.Page;
import org.jboss.resteasy.spi.NoLogWebApplicationException;
import org.tdakkota.ncproject.entities.Area;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/area")
public class AreaResource {
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Area get(@PathParam("id") Long id) {
        Area status = Area.findById(id);
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
        return Area.findAll().page(page).list();
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@Valid Area e) {
        e.persist();
        return Response.status(Response.Status.CREATED).entity(e).build();
    }

    @Transactional
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, @Valid Area e) {
        Area exist = Area.findById(id);
        if (exist == null) {
            e.persist();
            return Response.status(Response.Status.CREATED).entity(e).build();
        }

        Area result = exist.update(e);
        return Response.status(Response.Status.CREATED).entity(result).build();
    }

    @Transactional
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        if (!Area.deleteById(id)) {
            throw new NoLogWebApplicationException(404);
        }
    }
}
