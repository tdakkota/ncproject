package org.tdakkota.ncproject.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Area extends PanacheEntity {
    public String name = "";
    public String description = "";
}
