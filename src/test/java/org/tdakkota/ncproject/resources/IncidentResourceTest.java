package org.tdakkota.ncproject.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import org.tdakkota.ncproject.entities.Incident;
import org.tdakkota.ncproject.services.IncidentEventEmitter;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;

import static io.restassured.RestAssured.given;

@QuarkusTest
class IncidentResourceTest {
    @Inject
    IncidentResource service;

    @InjectMock
    IncidentEventEmitter mockEmitter;

    ValidatableResponse add(Incident body) {
        return given().when()
                .log().ifValidationFails()
                .contentType(ContentType.JSON)
                .body(body)
                .post("/incident")
                .then()
                .log().ifValidationFails();
    }

    @Test
    void addIncident() {
        // Bad empty Incident
        add(new Incident()).statusCode(400);

        Incident badDate = new Incident();
        badDate.name = "badDate";
        Instant now = Instant.now(); //current date
        badDate.timeline = new Incident.Timeline(now, now.minus(Duration.ofDays(300)));
        add(badDate).statusCode(400);

        Incident good = new Incident();
        good.name = "good";
        good.timeline = new Incident.Timeline(now.minus(Duration.ofDays(300)), now);
        add(good).statusCode(200);
    }
}