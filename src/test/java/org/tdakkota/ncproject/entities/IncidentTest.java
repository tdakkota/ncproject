package org.tdakkota.ncproject.entities;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IncidentTest {
    @Test
    void validate() {
        Locale.setDefault(Locale.ENGLISH);
        ValidatorFactory config = Validation.buildDefaultValidatorFactory();
        Validator validator = config.getValidator();

        assertFalse(validator.validate(new Incident()).isEmpty());
        Incident s = new Incident();
        s.setName("abc");
        s.setAssignee(new User());
        s.setArea(new Area());
        Status status = new Status();
        status.setBody(new StatusBody());
        s.setStatus(status);
        assertFalse(validator.validate(s).isEmpty());

        Instant now = Instant.now();
        s.setTimeline(new Timeline(now, now.minus(Duration.ofDays(300))));
        assertFalse(validator.validate(s).isEmpty());

        s.setTimeline(new Timeline(now.minus(Duration.ofDays(300)), now));
        assertTrue(validator.validate(s).isEmpty());
    }
}