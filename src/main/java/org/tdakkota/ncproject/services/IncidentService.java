package org.tdakkota.ncproject.services;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import org.jboss.logging.Logger;
import org.tdakkota.ncproject.entities.Incident;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.WebApplicationException;
import java.util.List;

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

    public List<Incident> list(Page page) {
        return Incident.findAll().page(page).list();
    }

    private Sort defaultSort() {
        return Sort.descending("priority", "start");
    }

    public List<Incident> getIncidentsByUser(Long id, Page page) {
        return getIncidentsByUser(id, page, defaultSort());
    }

    public List<Incident> getIncidentsByUser(Long id, Page page, Sort sort) {
        return Incident.find("from Incident as i where i.assignee.id = ?1", sort, id).
                page(page).
                list();
    }

    public List<Incident> getIncidentsByArea(Long id, Page page) {
        return getIncidentsByArea(id, page, defaultSort());
    }

    public List<Incident> getIncidentsByArea(Long id, Page page, Sort sort) {
        return Incident.find("from Incident as i where i.area.id = ?1", sort, id).
                page(page).
                list();
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
            this.emitter.opened(incidentToSave);
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
