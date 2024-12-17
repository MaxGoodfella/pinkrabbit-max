package com.max.producer.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
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

@Configuration
public class ProducerConfig {

    @Value("${RABBITMQ_QUEUE_NAME}")
    private String queueName;

    @Value("${RABBITMQ_DEFAULT_USER}")
    private String username;

    @Value("${RABBITMQ_DEFAULT_PASS}")
    private String password;

//    @Value("${RABBITMQ_ROUTING_KEY}")
//    private String routingKey;

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("rabbitmq");
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

//    @Bean
//    public Queue tasksQueue() {
//        return QueueBuilder.durable(queueName).build(); // устойчивая, если в дальнейшем нужно добавлять DDL или TTL
////        return new Queue(queueName, true); // устойчивая, если не нужны доп. настройки
//    }

    @Bean
    public Queue tasksQueue() {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", "tasks_dlq_exchange")
                .withArgument("x-dead-letter-routing-key", "tasks_dlq")
                .build();
    }

    @Bean
    public Queue tasksDlqQueue() {
        return QueueBuilder.durable("tasks_dlq").build();
    }

    @Bean
    public Queue priorityQueue() {
        return QueueBuilder.durable("priority_queue")
                .withArgument("x-max-priority", 10)
                .build();
    }

    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("delayed-exchange", "x-delayed-message", true, false, args);
    }

//    @Bean
//    public Binding binding(Queue tasksQueue, CustomExchange delayedExchange) {
//        return BindingBuilder.bind(tasksQueue).to(delayedExchange).with(routingKey).noargs();
//    }

    @Bean
    public Binding tasksBinding(Queue tasksQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(tasksQueue).to(delayedExchange).with("tasks_routing").noargs();
    }

    @Bean
    public Binding priorityBinding(Queue priorityQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(priorityQueue).to(delayedExchange).with("priority_routing").noargs();
    }

}