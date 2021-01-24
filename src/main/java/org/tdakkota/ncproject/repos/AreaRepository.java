package org.tdakkota.ncproject.repos;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.tdakkota.ncproject.entities.Area;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AreaRepository implements PanacheRepository<Area> {
    public Area update(Long id, Area e) {
        Area exist = findById(id);
        if (exist == null) {
            this.persist(e);
            return e;
        }

        exist.merge(e);
        this.persist(exist);
        return exist;
    }
}
