package com.max.producer.annotations;

import com.max.producer.exceptions.BadRequestException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DelayLimitValidator implements ConstraintValidator<DelayLimit, Integer> {

    private static final int MAX_DELAY = 600000;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value < 0 || value > MAX_DELAY) {
            throw new BadRequestException("Delay value must be between 0 and 600000 milliseconds (10 minutes).");
        }
        return true;
    }

}