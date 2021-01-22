package org.tdakkota.ncproject.services;

import org.jboss.logging.Logger;
import org.tdakkota.ncproject.entities.Incident;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.WebApplicationException;

@ApplicationScoped
public class IncidentService {
    @Inject
    IncidentEventEmitter emitter;

    @Inject
    Logger log;

    public Incident get(Long id) {
        Incident incident = Incident.findById(id);
        if (incident == null) {
            throw new WebApplicationException(404);
        }
        return incident;
    }

    @Transactional
    public Incident add(@Valid Incident incidentToSave) {
        incidentToSave.persist();

        this.log.debug("Incident created:" + incidentToSave.toString());
        this.emitter.opened(incidentToSave);
        return incidentToSave;
    }

    @Transactional
    public Incident update(@Valid Incident incidentToSave) {
        incidentToSave.persist();

        Incident exist = Incident.findById(incidentToSave.id);
        if (exist == null) {
            incidentToSave.persist();
            return incidentToSave;
        }

        Incident result = exist.getEntityManager().merge(incidentToSave);
        result.persist();

        this.log.debug("Incident updated:" + result.toString());
        this.emitter.updated(result);
        return result;
    }

    @Transactional
    public void close(Long id) {
        Incident incident = Incident.findById(id);
        if (incident == null) {
            throw new WebApplicationException(404);
        }
        incident.closed = true;
        incident.persist();

        this.log.debug("Incident closed:" + incident.toString());
        this.emitter.closed(incident);
    }
}
