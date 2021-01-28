package org.tdakkota.ncproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.constraints.Length;
import org.tdakkota.ncproject.api.UserSignUp;
import org.tdakkota.ncproject.constraints.Mergeable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@UserDefinition
@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User implements Mergeable<User> {
    @Id
    @GeneratedValue
    private Long id;

    @Username
    @NotBlank(message = Entities.BLANK_USERNAME_MESSAGE)
    @Length(max = Entities.USER_USERNAME_LENGTH_LIMIT)
    @Column(unique = true)
    @NonNull
    private String username;

    @NotBlank(message = Entities.BLANK_NAME_MESSAGE)
    @Pattern(regexp = Entities.USER_NAME_REGEXP)
    @Length(max = Entities.NAME_LENGTH_LIMIT)
    @NonNull
    private String name;

    @Roles
    private String role = Entities.USER_DEFAULT_ROLE;

    private Date createdAt = new Date();

    @OneToMany(mappedBy = "assignee")
    @ToString.Exclude
    @JsonIgnore
    private Set<Incident> assignedIncidents = Collections.emptySet();

    @Password
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @NonNull
    private String password;

    public User(UserSignUp signUp) {
        this.username = signUp.getUsername();
        this.name = signUp.getName();
        this.password = signUp.getPassword();
    }

    public void merge(User e) {
        this.username = e.username;
        this.password = e.password;
        this.name = e.name;
        this.role = e.role;
        this.createdAt = e.createdAt;
        this.assignedIncidents = new HashSet<>();
        if (CollectionUtils.isNotEmpty(e.assignedIncidents)) {
            this.assignedIncidents.addAll(e.assignedIncidents);
        }
    }

    @JsonProperty("password")
    public void setEncryptedPassword(String password) {
        this.password = BcryptUtil.bcryptHash(password);
    }
}
