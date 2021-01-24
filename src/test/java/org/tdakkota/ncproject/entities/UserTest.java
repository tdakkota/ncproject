package org.tdakkota.ncproject.entities;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {
    @Test
    void validate() {
        Locale.setDefault(Locale.ENGLISH);
        ValidatorFactory config = Validation.buildDefaultValidatorFactory();
        Validator validator = config.getValidator();

        assertFalse(validator.validate(new User()).isEmpty());
        User s = new User();
        s.setName("Admin");
        s.setUsername("admin");
        s.setEncryptedPassword("qwerty");
        assertTrue(validator.validate(s).isEmpty());

        s.setName("12345");
        assertFalse(validator.validate(s).isEmpty());
    }
}
