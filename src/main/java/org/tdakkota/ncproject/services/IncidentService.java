package org.tdakkota.ncproject.services;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.NoLogWebApplicationException;
import org.tdakkota.ncproject.api.AddIncidentRequest;
import org.tdakkota.ncproject.entities.Incident;
import org.tdakkota.ncproject.repos.IncidentRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@ApplicationScoped
public class IncidentService {
    @Inject
    IncidentRepository repo;

    @Inject
    IncidentEventEmitter emitter;

    @Inject
    Logger log;

    public Incident get(Long id) {
        Incident incident = repo.findById(id);
        if (incident == null) {
            throw new NoLogWebApplicationException(404);
        }
        return incident;
    }

    public List<Incident> list(Page page) {
        return repo.findAll().page(page).list();
    }

    public List<Incident> getIncidentsByUser(Long id, Page page) {
        return repo.getIncidentsByUser(id, page);
    }

    public List<Incident> getIncidentsByUser(Long id, Page page, Sort sort) {
        return repo.getIncidentsByUser(id, page, sort);
    }

    public List<Incident> getIncidentsByArea(Long id, Page page) {
        return repo.getIncidentsByArea(id, page);
    }

    public List<Incident> getIncidentsByArea(Long id, Page page, Sort sort) {
        return repo.getIncidentsByArea(id, page, sort);
    }

    @Transactional
    public Incident add(@Valid AddIncidentRequest req) {
        Incident incident = repo.add(req);
        this.log.debug("Incident created:" + incident.toString());
        this.emitter.opened(incident);
        return incident;
    }

    @Transactional
    public Incident update(Long id, @Valid AddIncidentRequest req) {
        Incident result = repo.update(id, req);
        this.log.debug("Incident updated:" + result.toString());
        this.emitter.updated(result);
        return result;
    }

    @Transactional
    public void close(Long id) {
        Incident incident = repo.findById(id);
        if (incident == null) {
            throw new NoLogWebApplicationException(404);
        }
        incident.setClosed(true);
        repo.persist(incident);

        this.log.debug("Incident closed:" + incident.toString());
        this.emitter.closed(incident);
    }
}
