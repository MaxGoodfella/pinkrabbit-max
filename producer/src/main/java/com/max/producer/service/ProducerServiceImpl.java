package com.max.producer.service;

import com.max.producer.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Service
public class ProducerServiceImpl implements ProducerService {

    private final RabbitTemplate rabbitTemplate;

//    @Override
//    public void sendMessage(String message, String routingKey, Integer delayInMillis) {
//
//        final int MAX_DELAY = 600000;
//
//        if (delayInMillis < 0 || delayInMillis > MAX_DELAY) {
//            throw new BadRequestException("Delay value must be between 0 and 600000 milliseconds (10 minutes).");
//        }
//
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("x-delay", delayInMillis);
//        Message amqpMessage = new Message(message.getBytes(StandardCharsets.UTF_8), properties);
//
//        rabbitTemplate.convertAndSend("delayed-exchange", routingKey, amqpMessage);
//    }

    @Override
    public void sendMessage(String message, String routingKey, Integer delayInMillis, Integer priority) {

        final int MAX_DELAY = 600000;

        if (delayInMillis < 0 || delayInMillis > MAX_DELAY) {
            throw new BadRequestException("Delay value must be between 0 and 600000 milliseconds (10 minutes).");
        }

        MessageProperties properties = new MessageProperties();
        properties.setHeader("x-delay", delayInMillis);

        if (priority != null) {
            properties.setPriority(priority);
        }

        Message amqpMessage = new Message(message.getBytes(StandardCharsets.UTF_8), properties);

        if (priority != null && priority > 0) {
            rabbitTemplate.convertAndSend("delayed-exchange", "priority_routing", amqpMessage);
        } else {
            rabbitTemplate.convertAndSend("delayed-exchange", "tasks_routing", amqpMessage);
        }

    }




}