package org.tdakkota.ncproject.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IncidentEvent {
    private EventType type;

    private long id;

    public static IncidentEvent opened(Incident i) {
        return new IncidentEvent(EventType.OPENED, i.getId());
    }

    public static IncidentEvent updated(Incident i) {
        return new IncidentEvent(EventType.UPDATED, i.getId());
    }

    public static IncidentEvent closed(Incident i) {
        return new IncidentEvent(EventType.CLOSED, i.getId());
    }

    public enum EventType {
        OPENED, UPDATED, CLOSED
    }
}
