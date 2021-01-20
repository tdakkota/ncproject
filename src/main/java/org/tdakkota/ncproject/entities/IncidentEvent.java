package org.tdakkota.ncproject.entities;

public class IncidentEvent {
    public enum EventType {
        OPENED, UPDATED, CLOSED
    }

    public EventType type;
    public long id;

    public IncidentEvent(EventType type, long id) {
        this.type = type;
        this.id = id;
    }

    public static IncidentEvent opened(Incident i) {
        return new IncidentEvent(EventType.OPENED, i.id);
    }

    public static IncidentEvent updated(Incident i) {
        return new IncidentEvent(EventType.UPDATED, i.id);
    }

    public static IncidentEvent closed(Incident i) {
        return new IncidentEvent(EventType.CLOSED, i.id);
    }
}
