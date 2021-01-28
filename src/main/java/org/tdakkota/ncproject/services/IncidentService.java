package org.tdakkota.ncproject.services;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.tdakkota.ncproject.api.AddIncidentRequest;
import org.tdakkota.ncproject.api.IncidentFilter;
import org.tdakkota.ncproject.entities.*;
import org.tdakkota.ncproject.misc.AddIncidentRequestMapper;
import org.tdakkota.ncproject.repos.AreaRepository;
import org.tdakkota.ncproject.repos.IncidentRepository;
import org.tdakkota.ncproject.repos.StatusRepository;
import org.tdakkota.ncproject.repos.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class IncidentService {
    @Inject
    IncidentRepository repo;

    @Inject
    AreaRepository areas;

    @Inject
    StatusRepository statuses;

    @Inject
    UserRepository users;

    @Inject
    AddIncidentRequestMapper addMapper;

    @Inject
    IncidentEventEmitter emitter;

    @Inject
    Logger log;

    public Optional<Incident> get(Long id) {
        return repo.findByIdOptional(id);
    }

    public List<Incident> list(int pageIndex, int pageSize) {
        return repo.findAll().page(Page.of(pageIndex, pageSize)).list();
    }

    public List<Incident> find(int pageIndex, int pageSize, IncidentFilter filter) {
        PanacheQuery<Incident> q = repo.findAll();
        for (Map.Entry<String, Parameters> e : filter.filters().entrySet()) {
            q = q.filter(e.getKey(), e.getValue());
        }
        return q.page(Page.of(pageIndex, pageSize)).list();
    }

    private void saveIncident(AddIncidentRequest req, Incident i) {
        addMapper.toIncident(req, i);

        User user = users.findByIdOptional(req.getAssigneeId()).orElseThrow(
                () -> new IncidentCreationException("area not found")
        );
        i.setAssignee(user);

        Area area = areas.findByIdOptional(req.getAreaId()).orElseThrow(
                () -> new IncidentCreationException("area not found")
        );
        i.setArea(area);

        Status status = statuses.findByIdOptional(req.getStatusId()).orElseThrow(
                () -> new IncidentCreationException("status not found")
        );
        i.setStatus(status);

        repo.persist(i);
    }

    @Transactional
    public Incident add(@Valid AddIncidentRequest req) {
        Incident incident = new Incident();
        saveIncident(req, incident);

        log.debug("Incident created:" + incident.toString());
        emitter.opened(incident);
        return incident;
    }

    @Transactional
    public Optional<Incident> update(Long id, @Valid AddIncidentRequest req) {
        return repo.findByIdOptional(id).map(exist -> {
            StatusBody from = exist.getStatus().getBody();
            List<Long> successors = from.getSuccessors();
            if (CollectionUtils.isNotEmpty(successors) && !successors.contains(req.getStatusId())) {
                String mustBe = from.getSuccessors().stream().map(Object::toString)
                        .collect(Collectors.joining(", "));
                throw new IncidentCreationException("invalid new status " + req.getStatusId() + ", must be one of " + mustBe);
            }

            saveIncident(req, exist);
            log.debug("Incident updated:" + exist.toString());
            emitter.updated(exist);
            return exist;
        });
    }

    @Transactional
    public boolean close(Long id) {
        return repo.findByIdOptional(id).
                map(incident -> {
                    incident.setClosed(true);
                    repo.persist(incident);

                    log.debug("Incident closed:" + incident.toString());
                    emitter.closed(incident);

                    return true;
                }).orElse(false);
    }
}
