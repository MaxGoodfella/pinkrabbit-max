package com.max.consumer.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс, представляющий структуру ответа при возникновении исключения.
 *
 * <p>Этот класс используется для формирования стандартизированного ответа с информацией об ошибках,
 * таких как описание ошибки, причина и статус. Также включает в себя временную метку для отслеживания времени
 * возникновения ошибки.</p>
 */
@Getter
@Builder
public class ExceptionResponse {

    /**
     * Список ошибок, связанных с запросом или выполнением операции.
     */
    private final List<Error> errors;

    /**
     * Сообщение, объясняющее, что именно пошло не так.
     */
    private final String message;

    /**
     * Причина возникновения ошибки, предоставляющая дополнительные детали о том, что вызвало сбой.
     */
    private final String reason;

    /**
     * Статус ошибки в виде строки, например, "400 Bad Request".
     */
    private final String status;

    /**
     * Временная метка, когда произошла ошибка.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp = LocalDateTime.now();

}