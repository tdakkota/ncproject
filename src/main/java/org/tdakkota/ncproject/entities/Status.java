package org.tdakkota.ncproject.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.tdakkota.ncproject.constraints.Mergeable;
import org.tdakkota.ncproject.constraints.NotRecursive;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "statuses")
@NotRecursive
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Status implements Mergeable<Status> {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Name may not be blank")
    @Length(max = 50)
    @NonNull
    private String name;

    @Length(max = 1000)
    private String description = "";

    @OneToMany(mappedBy = "id")
    @EqualsAndHashCode.Exclude
    private List<Status> successors = new ArrayList<>();

    public void merge(Status e) {
        this.name = e.name;
        this.description = e.description;
        this.successors = new ArrayList<>(e.successors);
    }

    @Override
    public String toString() {
        String successors = this.successors.stream().
                filter(a -> a != this)
                .map(Object::toString)
                .collect(Collectors.joining(","));
        return "Status{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", successors=[" + successors + "], id=" + id +
                '}';
    }
}
