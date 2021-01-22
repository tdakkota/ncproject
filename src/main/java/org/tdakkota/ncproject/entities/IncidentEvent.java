package org.tdakkota.ncproject.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
public class IncidentEvent {
    public EventType type;
    public long id;

    public static IncidentEvent opened(Incident i) {
        return new IncidentEvent(EventType.OPENED, i.id);
    }

    public static IncidentEvent updated(Incident i) {
        return new IncidentEvent(EventType.UPDATED, i.id);
    }

    public static IncidentEvent closed(Incident i) {
        return new IncidentEvent(EventType.CLOSED, i.id);
    }

    public enum EventType {
        OPENED, UPDATED, CLOSED
    }
}
