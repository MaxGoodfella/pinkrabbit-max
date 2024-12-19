package com.max.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Сервис для обработки сообщений из очереди RabbitMQ.
 * Этот сервис слушает сообщения, поступающие в очередь, обрабатывает их и логирует процесс.
 * В случае ошибки обработки сообщений выбрасывается исключение {@link AmqpRejectAndDontRequeueException},
 * что приводит к отклонению сообщения без повторной попытки.
 * <p>
 * Сервис использует аннотацию {@link RabbitListener} для прослушивания очереди сообщений.
 * </p>
 */
@Service
@Slf4j
public class ConsumerService {

    /**
     * Обрабатывает сообщения, поступающие в очередь RabbitMQ с именем, указанным в {@code RABBITMQ_QUEUE_NAME}.
     * После получения сообщения, сервис проверяет его на валидность, и если сообщение не пустое и не пустое,
     * выполняется его обработка. В случае ошибки выбрасывается {@link IllegalArgumentException} или другое исключение.
     * <p>
     * Если сообщение обрабатывается успешно, оно логируется как обработанное.
     * </p>
     *
     * @param message сообщение, полученное из очереди RabbitMQ
     * @throws AmqpRejectAndDontRequeueException если сообщение не может быть обработано
     */
    @RabbitListener(queues = {"${RABBITMQ_QUEUE_NAME}"}, containerFactory = "retryContainerFactory")
    public void consumeTask(String message) {
        log.info("Received message from tasks_queue: {}", message);

        try {
            Thread.sleep(5000);

            if (message == null || message.isEmpty() || message.isBlank()) {
                throw new IllegalArgumentException("Message is invalid");
            }

            log.info("Message from tasks_queue processed successfully: {}", message);

        } catch (Exception e) {
            log.error("Error processing message from tasks_queue: {}", message, e);
            throw new AmqpRejectAndDontRequeueException("Message processing failed");
        }
    }

//    /**
//     * Обрабатывает сообщения, поступающие в очередь RabbitMQ с именем, указанным в {@code RABBITMQ_QUEUE_NAME}.
//     * После получения сообщения, сервис проверяет его на валидность, и если сообщение не пустое и не пустое,
//     * выполняется его обработка. В случае ошибки выбрасывается {@link IllegalArgumentException} или другое исключение.
//     * <p>
//     * Если сообщение обрабатывается успешно, оно логируется как обработанное.
//     * </p>
//     *
//     * @param message сообщение, полученное из очереди RabbitMQ
//     * @throws AmqpRejectAndDontRequeueException если сообщение не может быть обработано
//     */
//    @RabbitListener(queues = {"priority_queue"}, containerFactory = "retryContainerFactory")
//    public void processPriorityQueue(String message) {
//        log.info("Received message from priority_queue: {}", message);
//
//        try {
//            if (message == null || message.isEmpty() || message.isBlank()) {
//                throw new IllegalArgumentException("Priority message is invalid");
//            }
//
//            log.info("Message from priority_queue processed successfully: {}", message);
//
//        } catch (Exception e) {
//            log.error("Error processing message from priority_queue: {}", message, e);
//            throw new AmqpRejectAndDontRequeueException("Priority message processing failed");
//        }
//    }

}