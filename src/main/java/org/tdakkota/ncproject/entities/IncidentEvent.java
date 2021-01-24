package org.tdakkota.ncproject.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
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

    @Override
    public String toString() {
        return "IncidentEvent{" +
                "type=" + type +
                ", id=" + id +
                '}';
    }

    public enum EventType {
        OPENED, UPDATED, CLOSED
    }
}
