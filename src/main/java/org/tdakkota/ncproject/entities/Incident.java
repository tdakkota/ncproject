package org.tdakkota.ncproject.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.tdakkota.ncproject.constraints.TimelineValid;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;

@Entity
@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "incidents")
public class Incident extends PanacheEntity {
    public String icon;

    @Length(max = 50)
    @NotBlank(message = "Name may not be blank")
    public String name;

    @ManyToOne(fetch = FetchType.LAZY)
    public User assignee;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    public Area area;

    @Embedded
    @TimelineValid
    @NotNull
    public Timeline timeline;

    @Length(max = 100)
    public String description;

    @Enumerated(EnumType.STRING)
    public Priority priority = Priority.NORMAL;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    public Status status;

    public boolean closed = false;

    @Override
    public String toString() {
        return "Incident{" +
                "icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", assignee=" + assignee +
                ", area=" + area +
                ", timeline=" + timeline +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", closed=" + closed +
                ", id=" + id +
                '}';
    }

    public enum Priority {
        BLOCKER, CRITICAL, MAJOR, NORMAL, MINOR
    }

    @Embeddable
    @EqualsAndHashCode(callSuper = false)
    public static class Timeline {
        @NotNull
        public Date start;
        @NotNull
        public Date due;

        public Timeline() {
        }

        public Timeline(Date due) {
            this.start = new Date();
            this.due = due;
        }

        public Timeline(Date start, Date due) {
            this.start = start;
            this.due = due;
        }

        public Timeline(Instant start, Instant due) {
            this.start = Date.from(start);
            this.due = Date.from(due);
        }

        @Override
        public String toString() {
            return "Timeline{" +
                    "start=" + start +
                    ", due=" + due +
                    '}';
        }

    }
}
