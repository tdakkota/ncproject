package org.tdakkota.ncproject.repos;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.tdakkota.ncproject.entities.Status;
import org.tdakkota.ncproject.entities.StatusBody;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.Date;

@ApplicationScoped
public class StatusRepository implements PanacheRepository<Status> {
    public Status persist(StatusBody e) {
        Status s = new Status();
        s.setBody(e);
        this.persist(s);
        return s;
    }

    public Status update(Long id, StatusBody e) {
        Status exist = findById(id);
        if (exist == null) {
            exist = new Status();
        }

        exist.setModificationDate(Date.from(Instant.now()));
        exist.setBody(e);
        this.persist(exist);
        return exist;
    }
}
