package org.tdakkota.ncproject.resources;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.tdakkota.ncproject.entities.Area;

import javax.inject.Inject;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestHTTPEndpoint(AreaResource.class)
class AreaResourceTest implements ResourceTest<Area> {
    @Inject
    AreaResource service;

    @Test
    void crudOps() {
        // Bad empty Area
        add(new Area()).statusCode(400);

        Area badName = new Area();
        badName.name = "badName".repeat(100);
        add(badName).statusCode(400);

        Area good = new Area();
        good.name = "good";
        Area createResponse = add(good)
                .statusCode(201)
                .extract()
                .as(Area.class);
        assertEquals(good, createResponse);

        Area getResponse = get(createResponse.id)
                .statusCode(200)
                .extract()
                .as(Area.class);
        assertEquals(createResponse, getResponse);

        Area update = new Area();
        update.name = "updateGood";
        Area updateResponse = update(createResponse.id, update)
                .statusCode(201)
                .extract()
                .as(Area.class);
        assertEquals(update, updateResponse);

        getResponse = get(createResponse.id)
                .statusCode(200)
                .extract()
                .as(Area.class);
        assertEquals(updateResponse, getResponse);

        Area[] listResponse = list().statusCode(200).extract().as(Area[].class);
        assertEquals(
                updateResponse,
                Stream.of(listResponse).
                        filter(i -> i.id.equals(createResponse.id)).
                        findFirst().orElseThrow()
        );

        delete(createResponse.id).statusCode(204);
        delete(createResponse.id).statusCode(404);

        get(createResponse.id).statusCode(404);
        listResponse = list().statusCode(200).extract().as(Area[].class);
        assertTrue(Stream.of(listResponse).noneMatch(i -> i.id.equals(createResponse.id)));
    }
}
