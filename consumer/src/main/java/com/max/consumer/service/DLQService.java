package com.max.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Сервис для обработки сообщений из очереди мертвых писем (Dead Letter Queue, DLQ).
 * Этот сервис слушает сообщения, поступающие в очередь "tasks_dlq", и логирует их для дальнейшего анализа.
 * <p>
 * При получении сообщения из DLQ, сервис просто выводит сообщение в логи.
 * </p>
 */
@Component
@Slf4j
public class DLQService {

    /**
     * Обрабатывает сообщения, поступающие в очередь "tasks_dlq".
     * <p>
     * Очередь используется для сообщений, которые не удалось обработать в основной очереди.
     * </p>
     *
     * @param message сообщение, полученное из очереди "tasks_dlq"
     */
    @RabbitListener(queues = "tasks_dlq", ackMode = "MANUAL")
    public void processDlqMessage(String message) {
        log.error("Received message in DLQ: {}", message);
    }

}