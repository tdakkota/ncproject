package org.tdakkota.ncproject.services;

import io.quarkus.panache.common.Page;
import org.tdakkota.ncproject.entities.Area;
import org.tdakkota.ncproject.repos.AreaRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AreaService {
    @Inject
    AreaRepository repo;

    public Optional<Area> get(Long id) {
        return repo.findByIdOptional(id);
    }

    public List<Area> list(int pageIndex, int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        return repo.findAll().page(page).list();
    }

    @Transactional
    public Area add(@Valid Area e) {
        repo.persist(e);
        return e;
    }

    @Transactional
    public Optional<Area> update(Long id, @Valid Area e) {
        return repo.update(id, e);
    }

    @Transactional
    public boolean delete(Long id) {
        return repo.deleteById(id);
    }
}
