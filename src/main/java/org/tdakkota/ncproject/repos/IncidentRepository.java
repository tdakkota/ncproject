package org.tdakkota.ncproject.repos;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.tdakkota.ncproject.entities.Incident;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class IncidentRepository implements PanacheRepository<Incident> {
}
