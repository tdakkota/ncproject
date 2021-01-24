package org.tdakkota.ncproject.constraints;

import org.tdakkota.ncproject.entities.Incident;
import org.tdakkota.ncproject.entities.Timeline;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimelineValidConstraintValidator implements ConstraintValidator<TimelineValid, Timeline> {
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
    public boolean isValid(Timeline value, ConstraintValidatorContext context) {
        return value != null &&
                value.getStart() != null &&
                value.getDue() != null &&
                value.getStart().before(value.getDue());
    }
}
