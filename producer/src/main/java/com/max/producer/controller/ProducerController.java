package com.max.producer.controller;

import com.max.producer.annotations.DelayLimit;
import com.max.producer.model.Message;
import com.max.producer.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ProducerController {

    private final ProducerService producerService;

    @PostMapping("/send")
    public void sendMessageToRabbit(@RequestBody Message message,
                                    @RequestParam(defaultValue = "0") Integer delay,
                                    @RequestParam(required = false) Integer priority) {
        log.info("Sending message '{}' with delay = {} and priority = {}", message, delay, priority);
        producerService.sendMessage(message.getMessage(), message.getRoutingKey(), delay, priority);
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

}
