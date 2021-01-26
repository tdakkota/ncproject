package org.tdakkota.ncproject.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.tdakkota.ncproject.constraints.TimelineValid;
import org.tdakkota.ncproject.entities.Entities;
import org.tdakkota.ncproject.entities.Incident;
import org.tdakkota.ncproject.entities.Timeline;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddIncidentRequest {
    private String icon;

    @Length(max = Entities.NAME_LENGTH_LIMIT)
    @NotBlank(message = Entities.BLANK_NAME_MESSAGE)
    private String name;

    @NotNull
    private Long assigneeId;

    @NotNull
    private Long areaId;

    @TimelineValid
    @NotNull
    private Timeline timeline;

    @Length(max = Entities.INCIDENT_DESCRIPTION_LENGTH_LIMIT)
    private String description;

    private Incident.Priority priority = Incident.Priority.NORMAL;

    @NotNull
    private Long statusId;

    private boolean closed;

    public AddIncidentRequest(String name, Long assignee, Long area, Long status, Timeline timeline) {
        this.name = name;
        this.assigneeId = assignee;
        this.areaId = area;
        this.statusId = status;
        this.timeline = timeline;
    }
}
