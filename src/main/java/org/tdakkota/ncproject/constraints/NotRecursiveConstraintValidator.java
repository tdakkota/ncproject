package org.tdakkota.ncproject.constraints;

import org.tdakkota.ncproject.entities.Status;
import org.tdakkota.ncproject.entities.StatusBody;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

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
        if (value == null || value.getBody() == null) {
            return false;
        }

        StatusBody body = value.getBody();
        List<Long> successors = body.getSuccessors();
        if (successors == null || successors.isEmpty()) {
            return true;
        }

        // Check that successors does not contain parent Status.
        return successors.stream().
                filter(Objects::nonNull).
                noneMatch(sub -> sub.equals(value.getId()));
    }
}
