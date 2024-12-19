package com.max.consumer.exceptions;

/**
 * Исключение, которое используется для представления ошибки "400 Bad Request".
 *
 * <p>Класс расширяет {@link RuntimeException} и предназначен для использования в приложениях,
 * где необходимо обработать случаи с некорректными запросами.</p>
 */
public class IllegalArgumentException extends RuntimeException {

    /**
     * Конструктор для создания исключения с указанным сообщением об ошибке.
     *
     * @param message Сообщение, которое будет отображаться в описании ошибки.
     *               Это сообщение обычно объясняет причину возникновения ошибки.
     */
    public IllegalArgumentException(String message) {
        super(message);
    }
}