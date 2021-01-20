package org.tdakkota.ncproject.resources;

import org.tdakkota.ncproject.entities.User;

import javax.transaction.Transactional;
import javax.ws.rs.*;

public class UserResource {
    @GET
    @Path("{id}")
    public User get(@PathParam("id") Long id) {
        User person = User.findById(id);
        if (person == null) {
            throw new WebApplicationException(404);
        }
        return person;
    }


    @Transactional
    @POST
    @Path("signup")
    public void signUp(@FormParam("username") String username, @FormParam("password") String password) {
        User.signUp(username, password, "user");
    }

    @Transactional
    @POST
    public User add(User personToSave) {
        personToSave.persist();
        return personToSave;
    }

    @Transactional
    @PUT
    @Path("{id}")
    public User update(User personToSave) {
        personToSave.persist();
        return personToSave;
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
