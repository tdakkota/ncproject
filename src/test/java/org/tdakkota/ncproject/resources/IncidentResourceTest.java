package org.tdakkota.ncproject.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tdakkota.ncproject.entities.Incident;
import org.tdakkota.ncproject.entities.IncidentEvent;
import org.tdakkota.ncproject.services.IncidentEventEmitter;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
class IncidentResourceTest {
    @Inject
    IncidentResource service;

    @InjectMock
    IncidentEventEmitter mockEmitter;
    Map<IncidentEvent.EventType, Incident> events = new HashMap<>();

    @BeforeEach
    void setUpMockEmitter() {
        Mockito.doAnswer(invocation -> {
            events.put(IncidentEvent.EventType.OPENED, invocation.getArgument(0));
            return null;
        }).when(mockEmitter).opened(any(Incident.class));

        Mockito.doAnswer(invocation -> {
            events.put(IncidentEvent.EventType.UPDATED, invocation.getArgument(0));
            return null;
        }).when(mockEmitter).updated(any(Incident.class));

        Mockito.doAnswer(invocation -> {
            events.put(IncidentEvent.EventType.CLOSED, invocation.getArgument(0));
            return null;
        }).when(mockEmitter).closed(any(Incident.class));
    }

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
    void crudOps() {
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
        Incident addResponse = add(good).statusCode(201).extract().as(Incident.class);
        assertEquals(addResponse, events.get(IncidentEvent.EventType.OPENED));
    }
}