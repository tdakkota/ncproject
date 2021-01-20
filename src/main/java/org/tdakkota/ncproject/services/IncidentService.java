package org.tdakkota.ncproject.services;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import org.tdakkota.ncproject.entities.Incident;
import org.tdakkota.ncproject.entities.IncidentEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

@ApplicationScoped
public class IncidentService {
    @Inject
    Logger log;

    @Inject
    @Channel("incidents")
    Emitter<IncidentEvent> incidentUpdates;

    private void sendAndAwait(IncidentEvent e) {
        incidentUpdates.send(e).
                toCompletableFuture().
                join();
    }

    private void opened(Incident incidentToSave) {
        this.sendAndAwait(IncidentEvent.opened(incidentToSave));
    }

    private void updated(Incident incidentToSave) {
        this.sendAndAwait(IncidentEvent.updated(incidentToSave));
    }

    private void closed(Incident incidentToSave) {
        this.sendAndAwait(IncidentEvent.closed(incidentToSave));
    }

    public Incident get(Long id) {
        Incident incident = Incident.findById(id);
        if (incident == null) {
            throw new WebApplicationException(404);
        }
        return incident;
    }

    @Transactional
    public Incident add(Incident incidentToSave) {
        incidentToSave.persist();

        this.log.debug("Incident created:" + incidentToSave.toString());
        this.opened(incidentToSave);
        return incidentToSave;
    }

    @Transactional
    public Incident update(Incident incidentToSave) {
        incidentToSave.persist();

        this.updated(incidentToSave);
        return incidentToSave;
    }

    @Transactional
    public void close(Long id) {
        Incident incident = Incident.findById(id);
        if (incident == null) {
            throw new WebApplicationException(404);
        }
        incident.closed = true;
        incident.persist();

        this.closed(incident);
    }
}
