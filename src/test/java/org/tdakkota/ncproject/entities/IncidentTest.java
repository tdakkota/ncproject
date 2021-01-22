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
        s.id = 10L;
        s.name = "abc";
        assertFalse(validator.validate(s).isEmpty());

        Instant now = Instant.now();
        s.timeline = new Incident.Timeline(now, now.minus(Duration.ofDays(300)));
        assertFalse(validator.validate(s).isEmpty());

        s.timeline = new Incident.Timeline(now.minus(Duration.ofDays(300)), now);
        assertTrue(validator.validate(s).isEmpty());
    }
}