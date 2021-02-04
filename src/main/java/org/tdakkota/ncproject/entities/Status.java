package org.tdakkota.ncproject.entities;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;

@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "statuses")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NonNull
    private Date creationDate = Date.from(Instant.now());

    private Date modificationDate;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @NotNull
    @NonNull
    @JsonUnwrapped
    private StatusBody body;
}
