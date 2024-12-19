package com.max.consumer;

import com.max.consumer.config.ConsumerConfig;
import com.max.consumer.controller.ConsumerPrefetchController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестовый класс для {@link ConsumerPrefetchController}.
 * Этот класс содержит тесты для проверки правильности работы контроллера,
 * который управляет prefetch count для потребителей.
 * Тесты проверяют как обработку запроса на обновление значения, так и на получение текущего значения.
 *
 * <p>
 * Для тестирования используется {@link MockMvc}.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
public class ConsumerPrefetchControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ConsumerConfig consumerConfig;

    @Mock
    private RabbitListenerEndpointRegistry registry;

    @InjectMocks
    private ConsumerPrefetchController consumerPrefetchController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(consumerPrefetchController).build();
    }

    /**
     * Тестирует метод {@link ConsumerPrefetchController#updatePrefetchCount(int)}.
     * Проверяет, что сервер вернет статус Bad Request, если передано значение, меньшее 1.
     *
     * @throws Exception если выполнение запроса завершилось с ошибкой
     */
    @Test
    public void updatePrefetchCount_ShouldReturnBadRequest_WhenCountIsLessThanOne() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/consumer/prefetch")
                        .param("count", "0"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Prefetch count must be at least 1"));
    }

    /**
     * Тестирует метод {@link ConsumerPrefetchController} {@code updatePrefetchCount(int)}.
     * Проверяет, что сервер обновит count и вернет статус OK, если передано корректное значение.
     *
     * @throws Exception если выполнение запроса завершилось с ошибкой
     */
    @Test
    public void updatePrefetchCount_ShouldUpdatePrefetchCount_WhenCountIsValid() throws Exception {
        int validCount = 5;

        doNothing().when(consumerConfig).updatePrefetchCount(validCount);
        doNothing().when(registry).stop();
        doNothing().when(registry).start();

        mockMvc.perform(MockMvcRequestBuilders.put("/consumer/prefetch")
                        .param("count", String.valueOf(validCount)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Prefetch count updated to: " + validCount));

        verify(consumerConfig).updatePrefetchCount(validCount);
        verify(registry).stop();
        verify(registry).start();
    }

    /**
     * Тестирует метод {@link ConsumerPrefetchController} {@code getPrefetchCount()}.
     * Проверяет, что сервер возвращает текущее значение.
     *
     * @throws Exception если выполнение запроса завершилось с ошибкой
     */
    @Test
    public void getPrefetchCount_ShouldReturnCurrentCount() throws Exception {
        int currentCount = 10;

        when(consumerConfig.getPrefetchCount()).thenReturn(currentCount);

        mockMvc.perform(MockMvcRequestBuilders.get("/consumer/prefetch"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Current prefetch count: " + currentCount));

        verify(consumerConfig).getPrefetchCount();
    }

}