package org.tdakkota.ncproject.entities;

import io.quarkus.elytron.security.common.BcryptUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
public class UserSignUp {
    @NotBlank(message = "Username may not be blank")
    @Length(max = 20)
    private String username;

    @NotBlank(message = "Password may not be blank")
    private String password;

    @NotBlank(message = "Name may not be blank")
    @Pattern(regexp = "^[a-zA-Z][\\sa-zA-Z]*$")
    private String name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BcryptUtil.bcryptHash(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
