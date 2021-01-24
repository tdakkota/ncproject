package org.tdakkota.ncproject.repos;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.tdakkota.ncproject.entities.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    public User update(Long id, User e) {
        User exist = findById(id);
        if (exist == null) {
            this.persist(e);
            return e;
        }

        exist.merge(e);
        this.persist(exist);
        return exist;
    }
}
