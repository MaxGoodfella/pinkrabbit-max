package com.max.producer;

import com.max.producer.annotations.DelayLimitValidator;
import com.max.producer.exceptions.BadRequestException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для валидатора DelayLimitValidator.
 */
class DelayLimitValidatorTest {

    private final DelayLimitValidator validator = new DelayLimitValidator();

    /**
     * Тестирует валидные задержки.
     * Проверяет, что задержки от 0 до 600000 миллисекунд считаются допустимыми.
     */
    @Test
    void testValidDelay() {
        assertTrue(validator.isValid(1000, null));
        assertTrue(validator.isValid(0, null));
        assertTrue(validator.isValid(600000, null));
    }

    /**
     * Тестирует задержку равную null.
     * Проверяет, что null считается допустимым значением.
     */
    @Test
    void testNullDelay() {
        assertTrue(validator.isValid(null, null));
    }

    /**
     * Тестирует слишком маленькую задержку.
     * Проверяет, что задержка меньше 0 приводит к BadRequestException.
     */
    @Test
    void testInvalidDelayTooLow() {
        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                validator.isValid(-1, null));
        assertEquals("Delay value must be between 0 and 600000 milliseconds (10 minutes).",
                exception.getMessage());
    }

    /**
     * Тестирует слишком большую задержку.
     * Проверяет, что задержка больше 600000 миллисекунд приводит к BadRequestException.
     */
    @Test
    void testInvalidDelayTooHigh() {
        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                validator.isValid(700000, null));
        assertEquals("Delay value must be between 0 and 600000 milliseconds (10 minutes).",
                exception.getMessage());
    }

}