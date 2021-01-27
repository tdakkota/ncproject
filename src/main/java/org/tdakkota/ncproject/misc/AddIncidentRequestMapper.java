package org.tdakkota.ncproject.misc;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.tdakkota.ncproject.api.AddIncidentRequest;
import org.tdakkota.ncproject.entities.Incident;

@Mapper(componentModel = "cdi")
public interface AddIncidentRequestMapper {
    void toIncident(AddIncidentRequest req, @MappingTarget Incident i);
}
