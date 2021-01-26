package org.tdakkota.ncproject.resources;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import org.tdakkota.ncproject.api.UserSignUp;
import org.tdakkota.ncproject.entities.User;

import javax.inject.Inject;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestHTTPEndpoint(UserResource.class)
class UserResourceTest implements ResourceTest<User> {
    @Inject
    UserResource service;

    public ValidatableResponse signup(UserSignUp signUp) {
        return resp(req().body(signUp)
                .post("/signup")
                .then());
    }

    @Test
    void crudOps() {
        // Bad empty User
        signup(new UserSignUp("", "", "")).statusCode(400);

        UserSignUp u = new UserSignUp("admin", "qwerty", "Admin");
        User createResponse = signup(u)
                .statusCode(201)
                .extract()
                .as(User.class);
        assertEquals(u.getUsername(), createResponse.getUsername());
        assertEquals(u.getName(), createResponse.getName());

        signup(u).statusCode(409);

        User getResponse = get(createResponse.getId())
                .statusCode(200)
                .extract()
                .as(User.class);
        assertEquals(createResponse, getResponse);

        User update = new User();
        update.setName("NewAdmin");
        update.setUsername("admin");
        User updateResponse = update(createResponse.getId(), update)
                .statusCode(200)
                .extract()
                .as(User.class);
        update.setId(updateResponse.getId());
        assertEquals(update, updateResponse);

        getResponse = get(createResponse.getId())
                .statusCode(200)
                .extract()
                .as(User.class);
        assertEquals(updateResponse, getResponse);

        User[] listResponse = list().statusCode(200).extract().as(User[].class);
        assertEquals(
                updateResponse,
                Stream.of(listResponse).
                        filter(i -> i.getId().equals(createResponse.getId())).
                        findFirst().orElseThrow()
        );

        delete(createResponse.getId()).statusCode(204);
        delete(createResponse.getId()).statusCode(404);

        get(createResponse.getId()).statusCode(404);
        listResponse = list().statusCode(200).extract().as(User[].class);
        assertTrue(Stream.of(listResponse).noneMatch(i -> i.getId().equals(createResponse.getId())));
    }
}
