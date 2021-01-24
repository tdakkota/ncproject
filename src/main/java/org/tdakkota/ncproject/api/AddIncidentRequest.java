package org.tdakkota.ncproject.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.tdakkota.ncproject.constraints.TimelineValid;
import org.tdakkota.ncproject.entities.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddIncidentRequest {
    private String icon;

    @Length(max = 50)
    @NotBlank(message = "Name may not be blank")
    private String name;

    @NotNull
    private Long assignee;

    @NotNull
    private Long area;

    @TimelineValid
    @NotNull
    private Timeline timeline;

    @Length(max = 100)
    private String description;

    private Incident.Priority priority = Incident.Priority.NORMAL;

    @NotNull
    private Long status;

    private boolean closed;

    public AddIncidentRequest(String name, User assignee, Area area, Status status, Timeline timeline) {
        this.name = name;
        this.assignee = assignee.getId();
        this.area = area.getId();
        this.status = status.getId();
        this.timeline = timeline;
    }
}
