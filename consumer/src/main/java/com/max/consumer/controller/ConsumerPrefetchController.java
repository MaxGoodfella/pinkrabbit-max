package com.max.consumer.controller;

import com.max.consumer.config.ConsumerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления количеством сообщений, которые потребитель RabbitMQ может забрать за один раз.
 * <p>Этот контроллер позволяет обновлять значение prefetchCount и получать текущее значение через HTTP-запросы.</p>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/consumer/prefetch")
public class ConsumerPrefetchController {

    private final ConsumerConfig consumerConfig;
    private final RabbitListenerEndpointRegistry registry;

    /**
     * Обновляет количество сообщений, которое потребитель может забрать за один раз.
     * <p>Этот метод позволяет динамически изменять значение prefetchCount для
     * управления производительностью потребителя.</p>
     *
     * @param count новое значение prefetchCount (количество сообщений)
     */
    @PutMapping
    public ResponseEntity<String> updatePrefetchCount(@RequestParam int count) {

        log.info("Updating prefetch count to {}", count);

        if (count < 1) {
            return ResponseEntity.badRequest().body("Prefetch count must be at least 1");
        }

        consumerConfig.updatePrefetchCount(count);

        registry.stop();
        registry.start();

        return ResponseEntity.ok("Prefetch count updated to: " + count);

    }

    /**
     * Получает текущее значение количества сообщений, которое потребитель может забрать за один раз.
     * <p>Этот метод позволяет получить текущее значение prefetchCount для мониторинга и диагностики.</p>
     */
    @GetMapping
    public ResponseEntity<String> getPrefetchCount() {
        log.info("Retrieving current prefetch count");
        int currentCount = consumerConfig.getPrefetchCount();
        return ResponseEntity.ok("Current prefetch count: " + currentCount);
    }

}