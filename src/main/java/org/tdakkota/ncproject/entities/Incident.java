package org.tdakkota.ncproject.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.validator.constraints.Length;
import org.tdakkota.ncproject.constraints.TimelineValid;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "incidents")
@FilterDef(name = "byAssigneeID", parameters = @ParamDef(name = "assigneeID", type = "long"))
@Filter(name = "byAssigneeID", condition = "assignee_id = :assigneeID")
@FilterDef(name = "byAreaID", parameters = @ParamDef(name = "areaID", type = "long"))
@Filter(name = "byAreaID", condition = "area_id = :areaID")
@FilterDef(name = "byStart", parameters = @ParamDef(name = "start", type = "date"))
@Filter(name = "byStart", condition = "start >= :start")
@FilterDef(name = "byDue", parameters = @ParamDef(name = "due", type = "date"))
@Filter(name = "byDue", condition = "due <= :due")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String icon;

    @Length(max = Entities.NAME_LENGTH_LIMIT)
    @NotBlank(message = Entities.BLANK_NAME_MESSAGE)
    @NonNull
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    private User assignee;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @NonNull
    private Area area;

    @Embedded
    @TimelineValid
    @NotNull
    @NonNull
    private Timeline timeline;

    @Length(max = Entities.INCIDENT_DESCRIPTION_LENGTH_LIMIT)
    private String description = "";

    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.NORMAL;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Status status;

    private boolean closed = false;

    public enum Priority {
        BLOCKER, CRITICAL, MAJOR, NORMAL, MINOR
    }
}
