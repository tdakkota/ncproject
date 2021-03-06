package org.tdakkota.ncproject.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotRecursiveConstraintValidator.class)
public @interface NotRecursive {
    String message() default "must not be self-referential";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}