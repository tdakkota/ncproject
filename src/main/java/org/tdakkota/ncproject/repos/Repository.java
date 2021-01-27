package org.tdakkota.ncproject.repos;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.tdakkota.ncproject.constraints.Mergeable;

import java.util.Optional;

public interface Repository<Entity extends Mergeable<Entity>> extends PanacheRepository<Entity> {
    default Optional<Entity> update(Long id, Entity e) {
        return findByIdOptional(id).map(exist -> {
            exist.merge(e);
            this.persist(exist);
            return exist;
        });
    }
}
