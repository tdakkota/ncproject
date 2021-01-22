package org.tdakkota.ncproject.entities;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Set;

@Entity
@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@UserDefinition
public class User extends PanacheEntity {
    @Username
    @NotBlank(message = "Username may not be blank")
    @Length(max = 20)
    public String username = "";

    @Password
    public String password = "";

    @Roles
    public String role;

    @Pattern(regexp = "^[a-zA-Z][\\sa-zA-Z]*$")
    @Length(max = 50)
    public String name = "";

    public Date createdAt = new Date();

    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Incident> assignedIncidents;

    public static User signUp(String username, String password, String role) {
        User user = new User();
        user.username = username;
        user.password = BcryptUtil.bcryptHash(password);
        user.role = role;
        user.persist();
        return user;
    }
}
