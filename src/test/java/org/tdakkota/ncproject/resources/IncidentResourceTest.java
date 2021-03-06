package org.tdakkota.ncproject.resources;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tdakkota.ncproject.api.AddIncidentRequest;
import org.tdakkota.ncproject.entities.*;
import org.tdakkota.ncproject.repos.AreaRepository;
import org.tdakkota.ncproject.repos.StatusRepository;
import org.tdakkota.ncproject.repos.UserRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
                this.user.getId(),
                this.area.getId(),
                this.status.get(0).getId(),
                timeline
        );

        Incident addResponse = add(good).statusCode(201).extract().as(Incident.class);

        Incident[] find = given()
                .queryParam("assigneeID", this.user.getId())
                .get("/find")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().as(Incident[].class);
        assertEquals(
                Stream.of(find).filter(e -> e.getId().equals(addResponse.getId())).findFirst().orElseThrow(),
                addResponse
        );

        find = given()
                .queryParam("areaID", this.area.getId())
                .get("/find")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().as(Incident[].class);
        assertEquals(
                Stream.of(find).filter(e -> e.getId().equals(addResponse.getId())).findFirst().orElseThrow(),
                addResponse
        );

        find = given()
                .queryParam("areaID", -1)
                .get("/find")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().as(Incident[].class);

        assertTrue(Stream.of(find).noneMatch(e -> e.getId().equals(addResponse.getId())));
    }

    ValidatableResponse add(AddIncidentRequest body) {
        return resp(req()
                .body(body)
                .post()
                .then());
    }
}