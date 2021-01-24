package org.tdakkota.ncproject.api;

import io.quarkus.elytron.security.common.BcryptUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserSignUp {
    @NotBlank(message = "Username may not be blank")
    @Length(max = 20)
    private String username;

    @NotBlank(message = "Password may not be blank")
    private String password;

    @NotBlank(message = "Name may not be blank")
    @Pattern(regexp = "^[a-zA-Z][\\sa-zA-Z]*$")
    private String name;

    public void setPassword(String password) {
        this.password = BcryptUtil.bcryptHash(password);
    }
}
