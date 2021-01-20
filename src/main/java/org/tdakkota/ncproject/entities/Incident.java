package org.tdakkota.ncproject.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
public class Incident extends PanacheEntity {
    public enum Priority {
        BLOCKER, CRITICAL, MAJOR, NORMAL, MINOR;
    }

    public String icon;

    @Length(max = 50)
    @NotBlank(message = "Name may not be blank")
    public String name;

    @ManyToOne(fetch = FetchType.LAZY)
    public User assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    public Area area;

    public Date start = new Date();

    public Date due;

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
                ", start=" + start +
                ", due=" + due +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", closed=" + closed +
                ", id=" + id +
                '}';
    }
}
