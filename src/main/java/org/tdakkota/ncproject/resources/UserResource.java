package org.tdakkota.ncproject.resources;

import org.tdakkota.ncproject.api.UserSignUp;
import org.tdakkota.ncproject.entities.User;
import org.tdakkota.ncproject.services.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user")
public class UserResource {
    @Inject
    UserService service;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User get(@PathParam("id") Long id) {
        return service.get(id).orElseThrow(NotFoundException::new);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> list(@QueryParam("page") @DefaultValue("0") int pageIndex,
                           @QueryParam("size") @DefaultValue("20") int pageSize) {
        return service.list(pageIndex, pageSize);
    }

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@Valid UserSignUp e) {
        return Response.status(Response.Status.CREATED).entity(service.signup(e)).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, @Valid User e) {
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
