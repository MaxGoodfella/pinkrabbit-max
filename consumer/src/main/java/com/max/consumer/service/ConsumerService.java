package com.max.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerService {

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

    @RabbitListener(queues = {"priority_queue"}, containerFactory = "retryContainerFactory")
    public void processPriorityQueue(String message) {
        log.info("Received message from priority_queue: {}", message);

        try {
            if (message == null || message.isEmpty() || message.isBlank()) {
                throw new IllegalArgumentException("Priority message is invalid");
            }

            log.info("Message from priority_queue processed successfully: {}", message);

        } catch (Exception e) {
            log.error("Error processing message from priority_queue: {}", message, e);
            throw new AmqpRejectAndDontRequeueException("Priority message processing failed");
        }
    }

}
