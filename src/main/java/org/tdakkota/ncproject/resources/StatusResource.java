package org.tdakkota.ncproject.resources;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import org.tdakkota.ncproject.entities.Area;

public interface StatusResource extends PanacheEntityResource<Area, Long> {
}
