package org.tdakkota.ncproject.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.tdakkota.ncproject.constraints.Mergeable;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatusBody implements Mergeable<StatusBody> {
    @NotBlank(message = "Name may not be blank")
    @Length(max = 50)
    @NonNull
    private String name;

    @Length(max = 1000)
    private String description = "";

    @OneToMany(mappedBy = "id")
    @EqualsAndHashCode.Exclude
    private List<Long> successors = new ArrayList<>();

    public void merge(StatusBody e) {
        this.name = e.name;
        this.description = e.description;
        this.successors = new ArrayList<>(e.successors);
    }

    @Override
    public String toString() {
        String successors = this.successors.stream().map(Object::toString)
                .collect(Collectors.joining(","));
        return "Status{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", successors=[" + successors + "]" +
                '}';
    }
}
