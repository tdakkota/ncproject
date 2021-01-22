package org.tdakkota.ncproject.constraints;

import org.tdakkota.ncproject.entities.Incident;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimelineValidConstraintValidator implements ConstraintValidator<TimelineValid, Incident.Timeline> {
    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(Incident.Timeline value, ConstraintValidatorContext context) {
        return value != null &&
                value.start != null &&
                value.due != null &&
                value.start.before(value.due);
    }
}
