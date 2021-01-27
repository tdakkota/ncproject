package org.tdakkota.ncproject.services;

import io.quarkus.panache.common.Page;
import org.tdakkota.ncproject.entities.Status;
import org.tdakkota.ncproject.entities.StatusBody;
import org.tdakkota.ncproject.repos.StatusRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class StatusService {
    @Inject
    StatusRepository repo;

    public Optional<Status> get(Long id) {
        return repo.findByIdOptional(id);
    }

    public List<Status> list(int pageIndex, int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        return repo.findAll().page(page).list();
    }

    @Transactional
    public Status add(@Valid StatusBody e) {
        return repo.persist(e);
    }

    @Transactional
    public Optional<Status> update(Long id, @Valid StatusBody e) {
        return repo.update(id, e);
    }

    @Transactional
    public boolean delete(Long id) {
        return repo.deleteById(id);
    }
}
