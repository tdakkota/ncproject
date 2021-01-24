package org.tdakkota.ncproject.resources;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tdakkota.ncproject.api.AddIncidentRequest;
import org.tdakkota.ncproject.entities.*;
import org.tdakkota.ncproject.repos.AreaRepository;
import org.tdakkota.ncproject.repos.StatusRepository;
import org.tdakkota.ncproject.repos.UserRepository;
import org.tdakkota.ncproject.services.IncidentEventEmitter;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
@TestHTTPEndpoint(IncidentResource.class)
class IncidentResourceTest implements ResourceTest<Incident> {
    @Inject
    IncidentResource service;

    @Inject
    AreaRepository areas;
    @Inject
    StatusRepository statuses;
    @Inject
    UserRepository users;

    @InjectMock
    IncidentEventEmitter mockEmitter;
    Map<IncidentEvent.EventType, Incident> events;

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

    private User user;
    private Area area;
    private List<Status> status = new ArrayList<>();

    @BeforeEach
    @Transactional
    public void setupTestData() {
        User user = new User();
        user.setUsername("testuser");
        user.setEncryptedPassword("testuser");
        user.setName("testuser");
        users.persist(user);
        this.user = user;

        Area area = new Area();
        area.setName("testarea");
        areas.persist(area);
        this.area = area;

        StatusBody statusBody = new StatusBody();
        statusBody.setName("teststatus2");
        Status s = statuses.persist(statusBody);
        this.status.add(s);

        long id = s.getId();
        statusBody = new StatusBody();
        statusBody.setName("teststatus");
        statusBody.setSuccessors(Collections.singletonList(id));
        this.status.add(statuses.persist(statusBody));
    }

    @Test
    void crudOps() {
        // Bad empty Incident
        add(new AddIncidentRequest()).statusCode(400);

        AddIncidentRequest badDate = new AddIncidentRequest();
        badDate.setName("badDate");
        Instant now = Instant.now(); //current date
        badDate.setTimeline(new Timeline(now, now.minus(Duration.ofDays(300))));
        add(badDate).statusCode(400);


        Timeline timeline = new Timeline(now.minus(Duration.ofDays(300)), now);
        AddIncidentRequest good = new AddIncidentRequest(
                "good",
                this.user,
                this.area,
                this.status.get(0),
                timeline
        );

        Incident addResponse = add(good).statusCode(201).extract().as(Incident.class);
        assertEquals(addResponse.getId(), events.get(IncidentEvent.EventType.OPENED).getId());
    }

    ValidatableResponse add(AddIncidentRequest body) {
        return resp(req()
                .body(body)
                .post()
                .then());
    }
}