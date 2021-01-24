package org.tdakkota.ncproject.resources;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

interface ResourceTest<Entity> {
    default RequestSpecification req() {
        // TODO(tdakkota): use specs instead, but it doesn't work good with QuarkusTest.
        //  Also there is no way to set ifValidationFails using RequestSpecBuilder.
        // See https://www.javadoc.io/doc/io.rest-assured/rest-assured/latest/io/restassured/builder/RequestSpecBuilder.html
        return given().when()
                .log().ifValidationFails()
                .contentType(ContentType.JSON);
    }

    default ValidatableResponse resp(ValidatableResponse r) {
        return r.log().ifValidationFails();
    }

    default ValidatableResponse add(Entity body) {
        return resp(req()
                .body(body)
                .post()
                .then());
    }

    default ValidatableResponse list() {
        return resp(req().get().then());
    }

    default ValidatableResponse get(Long id) {
        return resp(req().
                pathParam("id", id)
                .get("/{id}")
                .then());
    }

    default ValidatableResponse update(Long id, Entity body) {
        return resp(req()
                .body(body)
                .pathParam("id", id)
                .put("/{id}")
                .then());
    }

    default ValidatableResponse delete(Long id) {
        return resp(req()
                .pathParam("id", id)
                .delete("/{id}")
                .then());
    }
}
