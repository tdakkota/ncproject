package org.tdakkota.ncproject.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.tdakkota.ncproject.entities.Status;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class StatusResourceTest {
    @Inject
    StatusResource service;

    RequestSpecification req() {
        return given().when()
                .log().ifValidationFails()
                .contentType(ContentType.JSON);
    }

    ValidatableResponse add(Status body) {
        return req()
                .body(body)
                .post("/status")
                .then()
                .log().ifValidationFails()
                .contentType(ContentType.JSON);
    }

    ValidatableResponse list() {
        return req()
                .get("/status")
                .then()
                .log().ifValidationFails()
                .contentType(ContentType.JSON);
    }

    ValidatableResponse get(Long id) {
        return req()
                .pathParam("id", id)
                .get("/status/{id}")
                .then()
                .log().ifValidationFails()
                .contentType(ContentType.JSON);
    }

    ValidatableResponse update(Long id, Status body) {
        return req()
                .body(body)
                .pathParam("id", id)
                .put("/status/{id}")
                .then()
                .log().ifValidationFails()
                .contentType(ContentType.JSON);
    }

    @Test
    void crudOps() {
        // Bad empty Status
        add(new Status()).statusCode(400);

        Status badName = new Status();
        badName.name = "badName".repeat(100);
        add(badName).statusCode(400);

        Status good = new Status();
        good.name = "good";
        Status createResponse = add(good)
                .statusCode(201)
                .extract()
                .as(Status.class);
        assertEquals(good, createResponse);

        Status getResponse = get(createResponse.id)
                .statusCode(200)
                .extract()
                .as(Status.class);
        assertEquals(createResponse, getResponse);

        Status[] listResponse = list().statusCode(200).extract().as(Status[].class);
        System.out.println(Arrays.toString(listResponse));

        Status update = new Status();
        update.name = "updateGood";
        Status updateResponse = update(createResponse.id, update)
                .statusCode(201)
                .extract()
                .as(Status.class);
        assertEquals(update, updateResponse);

        getResponse = get(createResponse.id)
                .statusCode(200)
                .extract()
                .as(Status.class);
        assertEquals(updateResponse, getResponse);

        listResponse = list().statusCode(200).extract().as(Status[].class);
        System.out.println(Arrays.toString(listResponse));
        assertEquals(
                updateResponse,
                Stream.of(listResponse).
                        filter(i -> i.id.equals(createResponse.id)).
                        findFirst().orElseThrow()
        );
    }

    @Test
    void selfReferential() {
        Status good = new Status();
        good.name = "good";
        Status createResponse = add(good)
                .statusCode(201)
                .extract()
                .as(Status.class);
        assertEquals(good.name, createResponse.name);

        Status selfReferential = new Status();
        selfReferential.id = createResponse.id;
        selfReferential.name = createResponse.name;
        selfReferential.successors = Collections.singletonList(createResponse);
        update(createResponse.id, selfReferential).statusCode(400);

        selfReferential.successors = Collections.emptyList();
        update(createResponse.id, selfReferential).statusCode(201);
    }
}