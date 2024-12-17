package com.max.producer.service;

public interface ProducerService {

//    void sendMessage(String message, String routingKey, Integer delay);

    void sendMessage(String message, String routingKey, Integer delayInMillis, Integer priority);

}