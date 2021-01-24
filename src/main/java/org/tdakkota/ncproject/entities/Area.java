package org.tdakkota.ncproject.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.tdakkota.ncproject.constraints.Mergeable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tables")
public class Area implements Mergeable<Area> {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Name may not be blank")
    @Length(max = 50)
    private String name = "";

    @Length(max = 1000)
    private String description = "";

    public void merge(Area area) {
        this.name = area.name;
        this.description = area.description;
    }
}
