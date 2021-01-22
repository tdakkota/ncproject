package org.tdakkota.ncproject.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimelineValidConstraintValidator.class)
public @interface TimelineValid {
    String message() default "timeline is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}