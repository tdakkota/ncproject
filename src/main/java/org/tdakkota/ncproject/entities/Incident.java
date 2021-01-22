package org.tdakkota.ncproject.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.tdakkota.ncproject.constraints.TimelineValid;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.Date;

@Entity
@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "incidents")
public class Incident extends PanacheEntity {
    public String icon;
    @Length(max = 50)
    @NotBlank(message = "Name may not be blank")
    public String name;
    @ManyToOne(fetch = FetchType.LAZY)
    public User assignee;
    @ManyToOne(fetch = FetchType.LAZY)
    public Area area;
    @Embedded
    @TimelineValid
    public Timeline timeline;
    @Length(max = 100)
    public String description;
    @Enumerated(EnumType.STRING)
    public Priority priority;
    @ManyToOne(fetch = FetchType.LAZY)
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
    public static class Timeline {
        @NotEmpty
        public Date start;
        @NotEmpty
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
