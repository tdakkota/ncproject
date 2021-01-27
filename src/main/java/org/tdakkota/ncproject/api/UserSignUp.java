package org.tdakkota.ncproject.api;

import io.quarkus.elytron.security.common.BcryptUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.tdakkota.ncproject.entities.Entities;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserSignUp {
    @NotBlank(message = Entities.BLANK_USERNAME_MESSAGE)
    @Length(max = Entities.USER_USERNAME_LENGTH_LIMIT)
    private String username;

    @NotBlank(message = Entities.BLANK_PASSWORD_MESSAGE)
    private String password;

    @NotBlank(message = Entities.BLANK_NAME_MESSAGE)
    @Pattern(regexp = Entities.USER_NAME_REGEXP)
    private String name;

    public void setPassword(String password) {
        this.password = BcryptUtil.bcryptHash(password);
    }
}
