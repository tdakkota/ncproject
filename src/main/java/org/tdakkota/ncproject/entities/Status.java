package org.tdakkota.ncproject.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.tdakkota.ncproject.constraints.NotRecursive;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "statuses")
@NotRecursive
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Status extends PanacheEntity {
    @NotBlank(message = "Name may not be blank")
    @Length(max = 50)
    public String name = "";
    @Length(max = 1000)
    public String description = "";

    @OneToMany(mappedBy = "id")
    public List<Status> successors = new ArrayList<>();

    public Status update(Status e) {
        this.name = e.name;
        this.description = e.description;
        this.successors = new ArrayList<>(e.successors);
        this.persist();
        return this;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Status)) return false;
        Status status = (Status) o;
        return Objects.equals(name, status.name) &&
                Objects.equals(description, status.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, successors);
    }
}
