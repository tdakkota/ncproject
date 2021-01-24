package org.tdakkota.ncproject.resources;

import io.quarkus.panache.common.Page;
import org.jboss.resteasy.spi.NoLogWebApplicationException;
import org.tdakkota.ncproject.entities.User;
import org.tdakkota.ncproject.entities.UserSignUp;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user")
public class UserResource {
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User get(@PathParam("id") Long id) {
        User status = User.findById(id);
        if (status == null) {
            throw new NoLogWebApplicationException(Response.Status.NOT_FOUND);
        }
        return status;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> list(@QueryParam("page") @DefaultValue("0") int pageIndex,
                           @QueryParam("size") @DefaultValue("20") int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        return User.findAll().page(page).list();
    }

    @Transactional
    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@Valid UserSignUp signUp) {
        User u = User.find("from User as u where u.username = ?1", signUp.getUsername()).firstResult();
        if (u != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        User e = new User(signUp);
        e.persist();
        return Response.status(Response.Status.CREATED).entity(e).build();
    }


    @Transactional
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, @Valid User e) {
        User exist = User.findById(id);
        if (exist == null) {
            e.persist();
            return Response.status(Response.Status.CREATED).entity(e).build();
        }

        User result = exist.update(e);
        return Response.status(Response.Status.CREATED).entity(result).build();
    }

    @Transactional
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        if (!User.deleteById(id)) {
            throw new NoLogWebApplicationException(404);
        }
    }
}
