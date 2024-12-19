package com.max.producer;

import com.max.producer.service.ProducerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Тестирует метод {@link ProducerServiceImpl} {@code sendMessage(String, String, int)}.
 * <p>Этот класс тестирует метод {@code sendMessage} с различными сценариями:</p>
 */
@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ProducerServiceImpl producerService;

    /**
     * Тестирует метод {@link ProducerServiceImpl} {@code sendMessage(String, String, int)}.
     * Проверяется, что сообщение отправляется в очередь с правильной задержкой.
     */
    @Test
    void testSendMessage_withValidDelay_shouldSendMessage() {
        String message = "test message";
        String routingKey = "test routing key";
        int delayInMillis = 5000;

        producerService.sendMessageWithDelay(message, routingKey, delayInMillis);

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(rabbitTemplate, times(1)).convertAndSend(eq("delayed-exchange"), eq(routingKey), messageCaptor.capture());

        Message sentMessage = messageCaptor.getValue();
        assertNotNull(sentMessage);
        assertEquals(new String(sentMessage.getBody(), StandardCharsets.UTF_8), message);

        MessageProperties properties = sentMessage.getMessageProperties();
        assertEquals(Integer.valueOf(delayInMillis), properties.getHeader("x-delay"));
    }

}