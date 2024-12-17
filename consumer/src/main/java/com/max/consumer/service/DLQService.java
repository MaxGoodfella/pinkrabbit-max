package com.max.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DLQService {

    @RabbitListener(queues = "tasks_dlq", ackMode = "MANUAL")
    public void processDlqMessage(String message) {
        log.error("Received message in DLQ: {}", message);
    }

}