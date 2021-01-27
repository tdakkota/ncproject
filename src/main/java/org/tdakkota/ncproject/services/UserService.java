package org.tdakkota.ncproject.services;

import io.quarkus.panache.common.Page;
import org.tdakkota.ncproject.api.UserSignUp;
import org.tdakkota.ncproject.entities.User;
import org.tdakkota.ncproject.repos.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository repo;

    public Optional<User> get(Long id) {
        return repo.findByIdOptional(id);
    }

    public List<User> list(int pageIndex, int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        return repo.findAll().page(page).list();
    }

    @Transactional
    public User signup(@Valid UserSignUp signUp) {
        Optional<User> u = repo.find("from User as u where u.username = ?1", signUp.getUsername()).
                firstResultOptional();
        if (u.isPresent()) {
            throw new UserAlreadyExistsException("user @" + signUp.getUsername() +  " already exist");
        }

        User e = new User(signUp);
        repo.persist(e);
        return e;
    }

    @Transactional
    public Optional<User> update(Long id, @Valid User e) {
        return repo.update(id, e);
    }

    @Transactional
    public boolean delete(Long id) {
        return repo.deleteById(id);
    }
}
