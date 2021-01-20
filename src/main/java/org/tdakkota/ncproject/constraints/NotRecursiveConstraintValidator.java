package org.tdakkota.ncproject.constraints;

import org.tdakkota.ncproject.entities.Status;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotRecursiveConstraintValidator implements ConstraintValidator<NotRecursive, Status> {
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
    public boolean isValid(Status value, ConstraintValidatorContext context) {
        // Check that successors does not contain parent Status.
        return value.successors.stream().noneMatch(sub -> sub.id.equals(value.id));
    }
}
