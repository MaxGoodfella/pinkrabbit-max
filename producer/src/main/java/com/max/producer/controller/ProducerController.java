package com.max.producer.controller;

import com.max.producer.annotations.DelayLimit;
import com.max.producer.model.Message;
import com.max.producer.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для отправки сообщений и проверки состояния.
 * Предоставляет REST API для взаимодействия с производителем сообщений.
 * <p>
 * Этот контроллер позволяет отправлять сообщения в систему с заданным параметром задержки,
 * а также проверять состояние приложения. Для логирования используется аннотация {@code @Slf4j}.
 * </p>
 */
@Validated
@RequiredArgsConstructor
@RestController
@Slf4j
public class ProducerController {

    private final ProducerService producerService;

//    /**
//     * Отправляет сообщение с возможностью указания приоритета.
//     * <p>
//     * Этот метод получает сообщение в теле запроса и параметр приоритета.
//     * </p>
//     *
//     * @param message объект, содержащий сообщение и его маршрутизационный ключ.
//     * @param priority приоритетность (0 - 10).
//     */
//    @PostMapping("/send/priority")
//    public void sendMessageWithPriority(@RequestBody Message message,
//                                        @RequestParam(required = false) Integer priority) {
//        log.info("Sending message '{}' with priority = {}", message, priority);
//        producerService.sendMessageWithPriority(message.getMessage(), message.getRoutingKey(), priority);
//    }

    /**
     * Отправляет сообщение с возможностью указания задержки.
     * <p>
     * Этот метод получает сообщение в теле запроса и параметр задержки в миллисекундах.
     * Если задержка не указана, она по умолчанию равна 0.
     * </p>
     *
     * @param message объект, содержащий сообщение и его маршрутизационный ключ.
     * @param delay время задержки в миллисекундах перед отправкой сообщения. По умолчанию 0.
     * @throws {@code BadRequestException} если задержка выходит за пределы допустимого диапазона
     * (0 - 600000 миллисекунд (10 минут)) - аннотация {@link DelayLimit}
     */
    @PostMapping("/send/delay")
    public void sendMessageWithDelay(@RequestBody Message message,
                                     @RequestParam(defaultValue = "0") @DelayLimit Integer delay) {
        log.info("Sending message '{}' with delay = {}", message, delay);
        producerService.sendMessageWithDelay(message.getMessage(), message.getRoutingKey(), delay);
    }

    /**
     * Проверяет состояние приложения.
     * <p>
     * Этот метод возвращает строку "OK", чтобы подтвердить, что приложение работает.
     * </p>
     */
    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

}