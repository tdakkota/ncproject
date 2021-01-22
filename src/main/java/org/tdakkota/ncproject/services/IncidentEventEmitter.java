package org.tdakkota.ncproject.services;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.tdakkota.ncproject.entities.Incident;
import org.tdakkota.ncproject.entities.IncidentEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class IncidentEventEmitter {
    @Inject
    @Channel("incidents")
    Emitter<IncidentEvent> incidentUpdates;


    private void sendAndAwait(IncidentEvent e) {
        incidentUpdates.send(e).
                toCompletableFuture().
                join();
    }

    @Incoming("incidents")
    public void logSubscriber(IncidentEvent event) {
    }

    public void opened(Incident incidentToSave) {
        this.sendAndAwait(IncidentEvent.opened(incidentToSave));
    }

    public void updated(Incident incidentToSave) {
        this.sendAndAwait(IncidentEvent.updated(incidentToSave));
    }

    public void closed(Incident incidentToSave) {
        this.sendAndAwait(IncidentEvent.closed(incidentToSave));
    }
}
