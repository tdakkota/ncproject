package org.tdakkota.ncproject.resources;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tdakkota.ncproject.entities.AddIncidentRequest;
import org.tdakkota.ncproject.entities.Incident;
import org.tdakkota.ncproject.entities.IncidentEvent;
import org.tdakkota.ncproject.misc.Setup;
import org.tdakkota.ncproject.services.IncidentEventEmitter;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
@TestHTTPEndpoint(IncidentResource.class)
class IncidentResourceTest implements ResourceTest<Incident> {
    @Inject
    IncidentResource service;

    @InjectMock
    IncidentEventEmitter mockEmitter;
    Map<IncidentEvent.EventType, Incident> events;

    private Setup setup = new Setup();

    @BeforeEach
    void setUpMockEmitter() {
        this.events = new HashMap<>();

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

    @BeforeEach
    @Transactional
    public void setupTestData() {
        // Create test user, area and status
        setup.create();
    }

    @Test
    void crudOps() {
        // Bad empty Incident
        add(new AddIncidentRequest()).statusCode(400);

        AddIncidentRequest badDate = new AddIncidentRequest();
        badDate.name = "badDate";
        Instant now = Instant.now(); //current date
        badDate.timeline = new Incident.Timeline(now, now.minus(Duration.ofDays(300)));
        add(badDate).statusCode(400);


        AddIncidentRequest good = new AddIncidentRequest();
        good.name = "good";
        good.assignee = setup.getUser().id;
        good.area = setup.getArea().id;
        good.status = setup.getStatus().id;
        good.timeline = new Incident.Timeline(now.minus(Duration.ofDays(300)), now);

        Incident addResponse = add(good).statusCode(201).extract().as(Incident.class);
        assertEquals(addResponse, events.get(IncidentEvent.EventType.OPENED));
    }

    ValidatableResponse add(AddIncidentRequest body) {
        return resp(req()
                .body(body)
                .post()
                .then());
    }
}