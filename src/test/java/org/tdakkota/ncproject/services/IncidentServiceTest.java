package org.tdakkota.ncproject.services;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.tdakkota.ncproject.entities.Incident;

@QuarkusTest
class IncidentServiceTest {
    @Test
    void addIncident() {
        PanacheMock.mock(Incident.class);
    }
}