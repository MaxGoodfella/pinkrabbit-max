package com.max.producer.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация продюсера для работы с RabbitMQ.
 * <p>Этот класс настраивает соединение с RabbitMQ, обменники, очереди и биндинги для доставки сообщений
 * с поддержкой задержки и DLQ.</p>
 */
@Configuration
public class ProducerConfig {

    @Value("${RABBITMQ_QUEUE_NAME}")
    private String queueName;

//    @Value("${RABBITMQ_PRIORITY_QUEUE_NAME}")
//    private String priorityQueueName;

    @Value("${RABBITMQ_DEFAULT_USER}")
    private String username;

    @Value("${RABBITMQ_DEFAULT_PASS}")
    private String password;

    @Value("${RABBITMQ_ROUTING_KEY}")
    private String routingKey;

//    @Value("${RABBITMQ_PRIORITY_ROUTING_KEY}")
//    private String priorityRoutingKey;

    /**
     * Создает и настраивает фабрику соединений с RabbitMQ.
     */
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("rabbitmq");
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    /**
     * Создает объект {@link AmqpAdmin} для работы с метаданными RabbitMQ.
     */
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    /**
     * Создает {@link RabbitTemplate} для отправки сообщений в RabbitMQ.
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

//    @Bean
//    public Queue tasksQueue() {
//        return QueueBuilder.durable(queueName).build(); // устойчивая, если в дальнейшем нужно добавлять DDL или TTL
////        return new Queue(queueName, true); // устойчивая, если не нужны доп. настройки
//    }

    /**
     * Создает очередь для задач с поддержкой Dead Letter Queue.
     */
    @Bean
    public Queue tasksQueue() {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", "tasks_dlq_exchange")
                .withArgument("x-dead-letter-routing-key", "tasks_dlq")
                .build();
    }

    /**
     * Создает Dead Letter Queue.
     */
    @Bean
    public Queue tasksDlqQueue() {
        return QueueBuilder.durable("tasks_dlq").build();
    }

    /**
     * Создает обменник с поддержкой задержки сообщений.
     */
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("delayed-exchange", "x-delayed-message", true, false, args);
    }

    /**
     * Создает биндинг между очередью задач и обменником с задержкой.
     * <p>Очередь задач будет связана с обменником, и сообщения будут направляться в очередь
     * через указанный ключ маршрутизации.</p>
     */
    @Bean
    public Binding binding(Queue tasksQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(tasksQueue).to(delayedExchange).with(routingKey).noargs();
    }

//    /**
//     * Создает очередь с приоритетами.
//     */
//    @Bean
//    public Queue priorityQueue() {
//        return QueueBuilder.durable(priorityQueueName)
//                .withArgument("x-max-priority", 10)
//                .build();
//    }
//
//    /**
//     * Создает отдельный обменник для очереди с приоритетами.
//     */
////    @Bean
////    public DirectExchange priorityExchange() {
////        return new DirectExchange("priority_exchange");
////    }
//
//    @Bean
//    public CustomExchange priorityExchange() {
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-max-priority", 10);
//        return new CustomExchange("priority-exchange", "x-max-priority", true, false, args);
//
//
////        return new DirectExchange("priority_exchange");
//    }
//
//
//    /**
//     * Создает биндинг между очередью с приоритетами и её обменником.
//     */
//    @Bean
//    public Binding priorityBinding(Queue priorityQueue, CustomExchange priorityExchange) {
////    public Binding priorityBinding(Queue priorityQueue, DirectExchange priorityExchange) {
//        return BindingBuilder.bind(priorityQueue).to(priorityExchange).with(priorityRoutingKey).noargs();
//    }

}