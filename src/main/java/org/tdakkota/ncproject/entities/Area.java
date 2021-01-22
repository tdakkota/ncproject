package org.tdakkota.ncproject.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tables")
public class Area extends PanacheEntity {
    public String name = "";
    public String description = "";
}
