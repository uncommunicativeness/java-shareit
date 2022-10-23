package ru.practicum.shareit.booking.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Documented
@Target(TYPE)
@Constraint(validatedBy = DateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateConstraint {
    String message() default "Некорректная дата бронирования";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}