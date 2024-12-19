package com.max.producer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс, представляющий сообщение, которое будет отправлено в систему.
 * <p>
 * Этот класс используется для хранения текста сообщения и ключа маршрутизации.
 * </p>
 * <p>
 * Использует аннотации Lombok для автоматической генерации геттеров, сеттеров, конструкторов,
 * методов {@code toString()} и {@code Builder}.
 * </p>
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Message {

    /**
     * Текст сообщения.
     * <p>Это поле содержит сам текст сообщения, который будет отправлен.</p>
     */
    private String message;

    /**
     * Ключ маршрутизации.
     * <p>Это поле определяет ключ, по которому сообщение будет направлено в определённую очередь или канал.</p>
     */
    private String routingKey;

}