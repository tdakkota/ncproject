package org.tdakkota.ncproject.services;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.NoLogWebApplicationException;
import org.tdakkota.ncproject.entities.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
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
            throw new NoLogWebApplicationException(404);
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

    private Incident update(Incident i, AddIncidentRequest e) {
        i.icon = e.icon;
        i.name = e.name;
        i.timeline = e.timeline;
        i.description = e.description;
        i.priority = e.priority;
        i.closed = e.closed;

        User user = User.findById(e.assignee);
        if (user == null) {
            throw new IncidentServiceException("user not found");
        }
        i.assignee = user;

        Area area = Area.findById(e.area);
        if (area == null) {
            throw new IncidentServiceException("area not found");
        }
        i.area = area;

        Status status = Status.findById(e.status);
        if (status == null) {
            throw new IncidentServiceException("status not found");
        }
        i.status = status;

        i.persist();
        return i;
    }

    @Transactional
    public Incident add(@Valid AddIncidentRequest req) {
        Incident incident = update(new Incident(), req);
        this.log.debug("Incident created:" + incident.toString());
        this.emitter.opened(incident);
        return incident;
    }

    @Transactional
    public Incident update(Long id, @Valid AddIncidentRequest req) {
        Incident exist = Incident.findById(id);
        if (exist == null) {
            return add(req);
        }

        Incident result = update(exist, req);
        this.log.debug("Incident updated:" + result.toString());
        this.emitter.updated(result);
        return result;
    }

    @Transactional
    public void close(Long id) {
        Incident incident = Incident.findById(id);
        if (incident == null) {
            throw new NoLogWebApplicationException(404);
        }
        incident.closed = true;
        incident.persist();

        this.log.debug("Incident closed:" + incident.toString());
        this.emitter.closed(incident);
    }
}
