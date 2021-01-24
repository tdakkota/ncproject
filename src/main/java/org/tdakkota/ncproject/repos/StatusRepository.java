package org.tdakkota.ncproject.repos;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.tdakkota.ncproject.entities.Status;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StatusRepository implements PanacheRepository<Status> {
    public Status update(Long id, Status e) {
        Status exist = findById(id);
        if (exist == null) {
            this.persist(e);
            return e;
        }

        exist.merge(e);
        this.persist(exist);
        return exist;
    }
}
