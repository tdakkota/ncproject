package org.tdakkota.ncproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "users")
@UserDefinition
public class User extends PanacheEntity {
    @Username
    @NotBlank(message = "Username may not be blank")
    @Length(max = 20)
    @Column(unique = true)
    public String username;

    @NotBlank(message = "Name may not be blank")
    @Pattern(regexp = "^[a-zA-Z][\\sa-zA-Z]*$")
    @Length(max = 50)
    public String name;

    @Roles
    public String role = "user";
    public Date createdAt = new Date();

    @OneToMany(mappedBy = "assignee")
    @JsonIgnore
    public Set<Incident> assignedIncidents = Collections.emptySet();

    @Password
    @EqualsAndHashCode.Exclude
    private String password;

    public User(UserSignUp signUp) {
        this.username = signUp.getUsername();
        this.name = signUp.getName();
        this.password = signUp.getPassword();
    }

    public User update(User e) {
        this.username = e.username;
        this.password = e.password;
        this.name = e.name;
        this.role = e.role;
        this.createdAt = e.createdAt;
        this.assignedIncidents = new HashSet<>(e.assignedIncidents);
        this.persist();
        return this;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("password")
    public void setEncryptedPassword(String password) {
        this.password = BcryptUtil.bcryptHash(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                ", assignedIncidents=" + assignedIncidents +
                ", id=" + id +
                '}';
    }
}
