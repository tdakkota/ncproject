package org.tdakkota.ncproject.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.tdakkota.ncproject.constraints.Mergeable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "areas")
public class Area implements Mergeable<Area> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = Entities.BLANK_NAME_MESSAGE)
    @Length(max = Entities.NAME_LENGTH_LIMIT)
    private String name = "";

    @Length(max = Entities.DESCRIPTION_LENGTH_LIMIT)
    private String description = "";

    public void merge(Area area) {
        if (area != null) {
            this.name = area.getName();
            this.description = area.getDescription();
        }
    }
}
