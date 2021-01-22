package org.tdakkota.ncproject.resources;

import io.quarkus.panache.common.Page;
import org.tdakkota.ncproject.entities.User;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user")
public class UserResource {
    @Transactional
    @POST
    @Path("signup")
    public void signUp(@FormParam("username") String username, @FormParam("password") String password) {
        User.signUp(username, password, "user");
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public User get(@PathParam("id") Long id) {
        User status = User.findById(id);
        if (status == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return status;
    }

    @GET
    @Produces("application/json")
    public List<User> list(@QueryParam("page") @DefaultValue("0") int pageIndex,
                           @QueryParam("size") @DefaultValue("20") int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        return User.findAll().page(page).list();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response add(@Valid User e) {
        e.persist();
        return Response.ok(e).build();
    }

    @Transactional
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response update(@PathParam("id") Long id, @Valid User e) {
        User exist = User.findById(id);
        if (exist == null) {
            e.persist();
            return Response.status(Response.Status.NO_CONTENT).entity(e).build();
        }

        User result = exist.getEntityManager().merge(e);
        result.persist();
        return Response.status(Response.Status.CREATED).entity(result).build();
    }

    @Transactional
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        if (!User.deleteById(id)) {
            throw new WebApplicationException(404);
        }
    }
}
