package com.max.producer.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Класс для обработки исключений.
 *
 * <p>Этот класс перехватывает исключения и возвращает структурированный ответ с соответствующим HTTP статусом и
 * сообщением об ошибке.</p>
 */

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * Обработчик исключений для {@link IllegalArgumentException} и {@link BadRequestException}.
     * @param e Исключение, которое было выброшено.
     */
    @ExceptionHandler({IllegalArgumentException.class, BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBadRequestException(Exception e) {
        log.debug("400 Bad Request {}", e.getMessage(), e);
        return ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message(e.getMessage())
                .reason("Incorrect parameters")
                .build();
    }

    /**
     * Обработчик для любых других исключений {@link Throwable}.
     * @param e Исключение, которое было выброшено.
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleThrowable(Throwable e) {
        log.debug("Получен статус 500 Internal Server Error {}", e.getMessage(), e);
        return ExceptionResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .message(e.getMessage())
                .reason("Internal server error")
                .build();
    }

}