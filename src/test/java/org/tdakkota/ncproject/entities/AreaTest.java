package org.tdakkota.ncproject.entities;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AreaTest {
    @Test
    void validate() {
        Locale.setDefault(Locale.ENGLISH);
        ValidatorFactory config = Validation.buildDefaultValidatorFactory();
        Validator validator = config.getValidator();

        assertFalse(validator.validate(new Area()).isEmpty());
        Area s = new Area();
        s.id = 10L;
        s.name = "abc";
        assertTrue(validator.validate(s).isEmpty());
    }
}