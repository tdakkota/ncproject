package org.tdakkota.ncproject.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "tables")
public class Area extends PanacheEntity {
    @NotBlank(message = "Name may not be blank")
    @Length(max = 50)
    public String name = "";
    @Length(max = 1000)
    public String description = "";
}
