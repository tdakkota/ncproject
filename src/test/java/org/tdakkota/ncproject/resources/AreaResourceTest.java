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
        badName.setName("badName".repeat(100));
        add(badName).statusCode(400);

        Area good = new Area();
        good.setName("good");
        Area createResponse = add(good)
                .statusCode(201)
                .extract()
                .as(Area.class);
        good.setId(createResponse.getId());
        assertEquals(good, createResponse);

        Area getResponse = get(createResponse.getId())
                .statusCode(200)
                .extract()
                .as(Area.class);
        assertEquals(createResponse, getResponse);

        Area update = new Area();
        update.setName("updateGood");
        Area updateResponse = update(createResponse.getId(), update)
                .statusCode(200)
                .extract()
                .as(Area.class);
        update.setId(updateResponse.getId());
        assertEquals(update, updateResponse);

        getResponse = get(createResponse.getId())
                .statusCode(200)
                .extract()
                .as(Area.class);
        assertEquals(updateResponse, getResponse);

        Area[] listResponse = list().statusCode(200).extract().as(Area[].class);
        assertEquals(
                updateResponse,
                Stream.of(listResponse).
                        filter(i -> i.getId().equals(createResponse.getId())).
                        findFirst().orElseThrow()
        );

        delete(createResponse.getId()).statusCode(204);
        delete(createResponse.getId()).statusCode(404);

        get(createResponse.getId()).statusCode(404);
        listResponse = list().statusCode(200).extract().as(Area[].class);
        assertTrue(Stream.of(listResponse).noneMatch(i -> i.getId().equals(createResponse.getId())));
    }
}
