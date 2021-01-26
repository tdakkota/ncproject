package org.tdakkota.ncproject.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
public class Incident {
    @Id
    @GeneratedValue
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
