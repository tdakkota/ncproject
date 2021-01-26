package org.tdakkota.ncproject.resources;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.tdakkota.ncproject.entities.Status;
import org.tdakkota.ncproject.entities.StatusBody;

import javax.inject.Inject;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestHTTPEndpoint(StatusResource.class)
class StatusResourceTest implements ResourceTest<StatusBody> {
    @Inject
    StatusResource service;

    @Test
    void crudOps() {
        // Bad empty Status
        add(new StatusBody()).statusCode(400);

        StatusBody badName = new StatusBody();
        badName.setName("badName".repeat(100));
        add(badName).statusCode(400);

        StatusBody good = new StatusBody();
        good.setName("good");
        Status createResponse = add(good)
                .statusCode(201)
                .extract()
                .as(Status.class);
        assertEquals(good, createResponse.getBody());

        Status getResponse = get(createResponse.getId())
                .statusCode(200)
                .extract()
                .as(Status.class);
        assertEquals(createResponse, getResponse);

        StatusBody update = new StatusBody();
        update.setName("updateGood");
        Status updateResponse = update(createResponse.getId(), update)
                .statusCode(200)
                .extract()
                .as(Status.class);
        assertEquals(update, updateResponse.getBody());

        getResponse = get(createResponse.getId())
                .statusCode(200)
                .extract()
                .as(Status.class);
        assertEquals(updateResponse, getResponse);

        Status[] listResponse = list().statusCode(200).extract().as(Status[].class);
        assertEquals(
                updateResponse,
                Stream.of(listResponse).
                        filter(i -> i.getId().equals(createResponse.getId())).
                        findFirst().orElseThrow()
        );

        delete(createResponse.getId()).statusCode(204);
        delete(createResponse.getId()).statusCode(404);

        get(createResponse.getId()).statusCode(404);
        listResponse = list().statusCode(200).extract().as(Status[].class);
        assertTrue(Stream.of(listResponse).noneMatch(i -> i.getId().equals(createResponse.getId())));
    }
}