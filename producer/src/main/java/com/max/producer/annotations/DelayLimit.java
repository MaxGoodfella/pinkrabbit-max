package com.max.producer.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DelayLimitValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DelayLimit {
    String message() default "Delay must be less than {value}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String value() default "10 minutes (600000 ms)";
}