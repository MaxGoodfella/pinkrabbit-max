package com.max.consumer.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
//                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
//                .collect(Collectors.toList());
//
//        String message = String.join(", ", errors);
//
//        log.debug("400 Bad Request {}", message, ex);
//        return ExceptionResponse.builder()
//                .status(HttpStatus.BAD_REQUEST.toString())
//                .message(message)
//                .reason("Incorrect parameters")
//                .build();
//    }


    @ExceptionHandler({IllegalArgumentException.class, BadRequestException.class,
            MissingServletRequestParameterException.class, ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class,
            AmqpRejectAndDontRequeueException.class, IOException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBadRequestException(Exception e) {
        log.debug("400 Bad Request {}", e.getMessage(), e);
        return ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message(e.getMessage())
                .reason("Incorrect parameters")
                .build();
    }

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