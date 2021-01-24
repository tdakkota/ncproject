package org.tdakkota.ncproject.entities;

import org.hibernate.validator.constraints.Length;
import org.tdakkota.ncproject.constraints.TimelineValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AddIncidentRequest {
    public String icon;

    @Length(max = 50)
    @NotBlank(message = "Name may not be blank")
    public String name;

    public long assignee;

    public long area;

    @TimelineValid
    @NotNull
    public Incident.Timeline timeline;

    @Length(max = 100)
    public String description;

    public Incident.Priority priority = Incident.Priority.NORMAL;

    public long status;

    public boolean closed = false;
}
