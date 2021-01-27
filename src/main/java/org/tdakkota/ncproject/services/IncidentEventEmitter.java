package org.tdakkota.ncproject.services;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import org.tdakkota.ncproject.entities.Incident;
import org.tdakkota.ncproject.entities.IncidentEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class IncidentEventEmitter {
    @Inject
    @Broadcast
    @Channel("incidents")
    Emitter<IncidentEvent> incidentUpdates;

    @Inject
    Logger log;

    private void send(IncidentEvent e) {
        incidentUpdates.send(e).
                toCompletableFuture().
                join();
    }

    @Incoming("incidents")
    public void logSubscriber(IncidentEvent event) {
        log.info("got event: " + event.toString());
    }

    public void opened(Incident incidentToSave) {
        this.send(IncidentEvent.opened(incidentToSave));
    }

    public void updated(Incident incidentToSave) {
        this.send(IncidentEvent.updated(incidentToSave));
    }

    public void closed(Incident incidentToSave) {
        this.send(IncidentEvent.closed(incidentToSave));
    }
}
