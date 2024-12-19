package com.max.producer;

import com.max.producer.controller.ProducerController;
import com.max.producer.model.Message;
import com.max.producer.service.ProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Тесты для {@link ProducerController}.
 * <p>Используется Mockito для мокирования {@link ProducerService} и проверки правильности взаимодействия
 * с этим сервисом.</p>
 */
@ExtendWith(MockitoExtension.class)
class ProducerControllerTest {

    @Mock
    private ProducerService producerService;

    @InjectMocks
    private ProducerController producerController;

    /**
     * Тестирует метод {@link ProducerController} {@code sendMessageWithDelay(Message, int)}.
     * Проверяет, что метод {@link ProducerService} {@code sendMessageWithDelay(String, String, int)}.
     */
    @Test
    void testSendMessage() {
        Message message = new Message("testMessage", "testRoutingKey");
        int delayInMillis = 1000;

        producerController.sendMessageWithDelay(message, delayInMillis);

        verify(producerService, times(1))
                .sendMessageWithDelay(eq("testMessage"), eq("testRoutingKey"), eq(delayInMillis));
    }

    /**
     * Тестирует метод {@link ProducerController {@code healthCheck()}.
     * Проверяет, что метод возвращает правильный ответ "OK".
     */
    @Test
    void testHealthCheck() {
        String response = producerController.healthCheck();

        assertEquals("OK", response);
    }

}