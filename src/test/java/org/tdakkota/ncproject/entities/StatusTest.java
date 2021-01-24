package org.tdakkota.ncproject.entities;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatusTest {
    @Test
    void validate() {
        Locale.setDefault(Locale.ENGLISH);
        ValidatorFactory config = Validation.buildDefaultValidatorFactory();
        Validator validator = config.getValidator();

        assertFalse(validator.validate(new Status()).isEmpty());
        Status s = new Status();
        s.setId(10L);
        s.setName("abc");
        s.setSuccessors(Collections.emptyList());
        assertTrue(validator.validate(s).isEmpty());

        s.setSuccessors(Collections.singletonList(s));
        assertFalse(validator.validate(s).isEmpty());
    }
}