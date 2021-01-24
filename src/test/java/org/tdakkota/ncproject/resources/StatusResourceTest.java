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
        badName.setName("badName".repeat(100));
        add(badName).statusCode(400);

        Status good = new Status();
        good.setName("good");
        Status createResponse = add(good)
                .statusCode(201)
                .extract()
                .as(Status.class);
        good.setId(createResponse.getId());
        assertEquals(good, createResponse);

        Status getResponse = get(createResponse.getId())
                .statusCode(200)
                .extract()
                .as(Status.class);
        assertEquals(createResponse, getResponse);

        Status update = new Status();
        update.setName("updateGood");
        Status updateResponse = update(createResponse.getId(), update)
                .statusCode(201)
                .extract()
                .as(Status.class);
        update.setId(updateResponse.getId());
        assertEquals(update, updateResponse);


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

    @Test
    void selfReferential() {
        Status good = new Status();
        good.setName("good");
        Status createResponse = add(good)
                .statusCode(201)
                .extract()
                .as(Status.class);
        assertEquals(good.getName(), createResponse.getName());

        Status selfReferential = new Status();
        selfReferential.setId(createResponse.getId());
        selfReferential.setName(createResponse.getName());
        selfReferential.setSuccessors(Collections.singletonList(createResponse));
        update(createResponse.getId(), selfReferential).statusCode(400);

        selfReferential.setSuccessors(Collections.emptyList());
        update(createResponse.getId(), selfReferential).statusCode(201);
    }
}