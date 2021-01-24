package org.tdakkota.ncproject.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;

@RegisterForReflection
@NoArgsConstructor
@Data
@Embeddable
public class Timeline {
    @NotNull
    @NonNull
    private Date start;

    @NotNull
    @NonNull
    private Date due;

    public Timeline(Date due) {
        this.start = new Date();
        this.due = due;
    }

    public Timeline(Instant start, Instant due) {
        this.start = Date.from(start);
        this.due = Date.from(due);
    }
}
