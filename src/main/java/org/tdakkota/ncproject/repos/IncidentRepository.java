package org.tdakkota.ncproject.repos;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import org.tdakkota.ncproject.api.AddIncidentRequest;
import org.tdakkota.ncproject.entities.Area;
import org.tdakkota.ncproject.entities.Incident;
import org.tdakkota.ncproject.entities.Status;
import org.tdakkota.ncproject.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;


@ApplicationScoped
public class IncidentRepository implements PanacheRepository<Incident> {
    @Inject
    AreaRepository areas;

    @Inject
    StatusRepository statuses;

    @Inject
    UserRepository users;

    private void fillFrom(Incident i, AddIncidentRequest e) {
        i.setIcon(e.getIcon());
        i.setName(e.getName());
        i.setTimeline(e.getTimeline());
        i.setDescription(e.getDescription());
        i.setPriority(e.getPriority());
        i.setClosed(e.isClosed());

        User user = users.findById(e.getAssignee());
        if (user == null) {
            throw new IncidentRepositoryException("user not found");
        }
        i.setAssignee(user);

        Area area = areas.findById(e.getArea());
        if (area == null) {
            throw new IncidentRepositoryException("area not found");
        }
        i.setArea(area);

        Status status = statuses.findById(e.getStatus());
        if (status == null) {
            throw new IncidentRepositoryException("status not found");
        }
        i.setStatus(status);
    }

    public Incident add(AddIncidentRequest e) {
        Incident toPersist = new Incident();
        fillFrom(toPersist, e);
        this.persist(toPersist);
        return toPersist;
    }

    public Incident update(Long id, AddIncidentRequest e) {
        Incident exist = findById(id);
        if (exist == null) {
            exist = new Incident();
        }

        fillFrom(exist, e);
        this.persist(exist);
        return exist;
    }

    private Sort defaultSort() {
        return Sort.descending("priority", "start");
    }

    public List<Incident> getIncidentsByUser(Long id, Page page) {
        return getIncidentsByUser(id, page, defaultSort());
    }

    public List<Incident> getIncidentsByUser(Long id, Page page, Sort sort) {
        return this.find("from Incident as i where i.assignee.id = ?1", sort, id).
                page(page).
                list();
    }

    public List<Incident> getIncidentsByArea(Long id, Page page) {
        return getIncidentsByArea(id, page, defaultSort());
    }

    public List<Incident> getIncidentsByArea(Long id, Page page, Sort sort) {
        return this.find("from Incident as i where i.area.id = ?1", sort, id).
                page(page).
                list();
    }
}
