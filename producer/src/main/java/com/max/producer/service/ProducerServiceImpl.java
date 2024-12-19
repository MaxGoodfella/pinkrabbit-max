package com.max.producer.service;

import com.max.producer.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Реализация сервиса отправки сообщений.
 *
 * <p>Этот класс реализует интерфейс {@link ProducerService} и отвечает за отправку сообщений
 * в очередь RabbitMQ с возможностью задания задержки перед отправкой.</p>
 */

@RequiredArgsConstructor
@Service
public class ProducerServiceImpl implements ProducerService {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Отправляет сообщение в очередь RabbitMQ с указанной задержкой.
     *
     * <p>Этот метод использует {@link RabbitTemplate} для отправки сообщения в очередь с заданным
     * маршрутом и задержкой в миллисекундах. Если задержка не входит в допустимые рамки, выбрасывается
     * исключение {@link BadRequestException}.</p>
     *
     * @param message Текст сообщения, которое будет отправлено.
     * @param routingKey Ключ маршрутизации для определения целевой очереди.
     * @param delayInMillis Задержка в миллисекундах перед отправкой сообщения.
     */
    @Override
    public void sendMessageWithDelay(String message, String routingKey, Integer delayInMillis) {
        MessageProperties properties = new MessageProperties();
        properties.setHeader("x-delay", delayInMillis);
        Message amqpMessage = new Message(message.getBytes(StandardCharsets.UTF_8), properties);

        rabbitTemplate.convertAndSend("delayed-exchange", routingKey, amqpMessage);
    }

//    /**
//     * Отправляет сообщение в очередь RabbitMQ с указанной задержкой.
//     *
//     * <p>Этот метод использует {@link RabbitTemplate} для отправки сообщения в очередь с заданным
//     * маршрутом и задержкой в миллисекундах. Если задержка не входит в допустимые рамки, выбрасывается
//     * исключение {@link BadRequestException}.</p>
//     *
//     * @param message Текст сообщения, которое будет отправлено.
//     * @param routingKey Ключ маршрутизации для определения целевой очереди.
//     * @param priority Приоритетность сообщения (0 - 10).
//     * @throws BadRequestException если задержка выходит за пределы допустимого диапазона
//     * (0 - 600000 миллисекунд (10 минут)).
//     */
//    @Override
//    public void sendMessageWithPriority(String message, String routingKey, Integer priority) {
//        final int MAX_PRIORITY = 10;
//
//        if (priority == null) {
//            priority = 0;
//        }
//
//        if (priority < 0 || priority > MAX_PRIORITY) {
//            throw new BadRequestException("Priority value must be between 0 and " + MAX_PRIORITY);
//        }
//
//        MessageProperties properties = new MessageProperties();
//        properties.setPriority(priority);
//        Message amqpMessage = new Message(message.getBytes(StandardCharsets.UTF_8), properties);
//
//        rabbitTemplate.convertAndSend("priority-exchange", routingKey, amqpMessage);
//    }

}