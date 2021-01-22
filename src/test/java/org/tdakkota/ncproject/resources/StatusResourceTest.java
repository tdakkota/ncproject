package org.tdakkota.ncproject.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import org.tdakkota.ncproject.entities.Status;

import javax.inject.Inject;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class StatusResourceTest {
    @Inject
    StatusResource service;

    ValidatableResponse add(Status body) {
        return given().when()
                .log().ifValidationFails()
                .contentType(ContentType.JSON)
                .body(body)
                .post("/status")
                .then()
                .log().ifValidationFails()
                .contentType(ContentType.JSON);
    }

    ValidatableResponse get(Long id) {
        return given().when()
                .log().ifValidationFails()
                .pathParam("id", id)
                .get("/status/{id}")
                .then()
                .log().ifValidationFails()
                .contentType(ContentType.JSON);
    }

    ValidatableResponse update(Long id, Status body) {
        return given().when()
                .log().ifValidationFails()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("id", id)
                .put("/status/{id}")
                .then()
                .log().ifValidationFails()
                .contentType(ContentType.JSON);
    }

    @Test
    void addAndGetStatus() {
        // Bad empty Status
        add(new Status()).statusCode(400);

        Status badName = new Status();
        badName.name = "badName".repeat(100);
        add(badName).statusCode(400);

        Status good = new Status();
        good.name = "good";
        Status createResponse = add(good)
                .statusCode(200)
                .extract()
                .as(Status.class);
        assertEquals(good.name, createResponse.name);

        Status getResponse = get(createResponse.id)
                .statusCode(200)
                .extract()
                .as(Status.class);
        assertEquals(createResponse.name, getResponse.name);
    }

    @Test
    void selfReferential() {
        Status good = new Status();
        good.name = "good";
        Status createResponse = add(good)
                .statusCode(200)
                .extract()
                .as(Status.class);
        assertEquals(good.name, createResponse.name);

        Status selfReferential = new Status();
        selfReferential.id = createResponse.id;
        selfReferential.name = createResponse.name;
        selfReferential.successors = Collections.singletonList(createResponse);
        update(createResponse.id, selfReferential).statusCode(400);
    }
}