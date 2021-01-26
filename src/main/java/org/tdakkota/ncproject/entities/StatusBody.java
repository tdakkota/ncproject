package org.tdakkota.ncproject.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.constraints.Length;
import org.tdakkota.ncproject.constraints.Mergeable;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatusBody implements Mergeable<StatusBody> {
    @NotBlank(message = Entities.BLANK_NAME_MESSAGE)
    @Length(max = Entities.NAME_LENGTH_LIMIT)
    @NonNull
    private String name;

    @Length(max = Entities.DESCRIPTION_LENGTH_LIMIT)
    private String description = "";

    @OneToMany(mappedBy = "id")
    @EqualsAndHashCode.Exclude
    private List<Long> successors = new ArrayList<>();

    public void merge(StatusBody e) {
        if (e != null) {
            this.name = e.getName();
            this.description = e.getDescription();
            if (CollectionUtils.isNotEmpty(e.getSuccessors())) {
                this.successors = e.successors.stream().
                        filter(Objects::nonNull).
                        collect(Collectors.toList());
            }
        }
    }

    @Override
    public String toString() {
        String successors = this.successors.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        return "Status{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", successors=[" + successors + "]" +
                '}';
    }
}
