package org.tdakkota.ncproject.resources;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import org.tdakkota.ncproject.entities.User;
import org.tdakkota.ncproject.entities.UserSignUp;

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
        assertEquals(u.getUsername(), createResponse.username);
        assertEquals(u.getName(), createResponse.name);

        signup(u).statusCode(409);

        User getResponse = get(createResponse.id)
                .statusCode(200)
                .extract()
                .as(User.class);
        assertEquals(createResponse, getResponse);

        User update = new User();
        update.name = "NewAdmin";
        update.username = "admin";
        User updateResponse = update(createResponse.id, update)
                .statusCode(201)
                .extract()
                .as(User.class);
        assertEquals(update, updateResponse);

        getResponse = get(createResponse.id)
                .statusCode(200)
                .extract()
                .as(User.class);
        assertEquals(updateResponse, getResponse);

        User[] listResponse = list().statusCode(200).extract().as(User[].class);
        assertEquals(
                updateResponse,
                Stream.of(listResponse).
                        filter(i -> i.id.equals(createResponse.id)).
                        findFirst().orElseThrow()
        );

        delete(createResponse.id).statusCode(204);
        delete(createResponse.id).statusCode(404);

        get(createResponse.id).statusCode(404);
        listResponse = list().statusCode(200).extract().as(User[].class);
        assertTrue(Stream.of(listResponse).noneMatch(i -> i.id.equals(createResponse.id)));
    }
}
