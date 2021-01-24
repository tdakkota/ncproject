package org.tdakkota.ncproject.resources;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.tdakkota.ncproject.entities.Status;

import javax.inject.Inject;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestHTTPEndpoint(StatusResource.class)
class StatusResourceTest implements ResourceTest<Status> {
    @Inject
    StatusResource service;

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

        Status[] listResponse = list().statusCode(200).extract().as(Status[].class);
        assertEquals(
                updateResponse,
                Stream.of(listResponse).
                        filter(i -> i.id.equals(createResponse.id)).
                        findFirst().orElseThrow()
        );

        delete(createResponse.id).statusCode(204);
        delete(createResponse.id).statusCode(404);

        get(createResponse.id).statusCode(404);
        listResponse = list().statusCode(200).extract().as(Status[].class);
        assertTrue(Stream.of(listResponse).noneMatch(i -> i.id.equals(createResponse.id)));
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