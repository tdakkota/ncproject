package org.tdakkota.ncproject.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.validator.constraints.Length;
import org.tdakkota.ncproject.constraints.NotRecursive;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Status extends PanacheEntity {
    @NotBlank(message = "Name may not be blank")
    @Length(max = 50)
    public String name = "";
    @Length(max = 1000)
    public String description = "";

    @OneToMany(mappedBy = "id")
    @NotRecursive
    public List<Status> successors = new ArrayList<>();
}
