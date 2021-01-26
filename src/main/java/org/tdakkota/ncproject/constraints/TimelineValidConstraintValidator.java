package org.tdakkota.ncproject.constraints;

import org.tdakkota.ncproject.entities.Timeline;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.Optional;

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
        return Optional.ofNullable(value)
                .filter(v -> Objects.nonNull(v.getStart()))
                .filter(v -> Objects.nonNull(v.getDue()))
                .map(v -> value.getStart().before(value.getDue()))
                .orElse(false);
    }
}
