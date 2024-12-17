package com.max.consumer.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ConsumerConfig {

    @Value("${RABBITMQ_QUEUE_NAME}")
    private String queueName;

    private final AtomicInteger prefetchCount = new AtomicInteger(10);

    @Bean
    public SimpleRabbitListenerContainerFactory retryContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        factory.setAdviceChain(retryInterceptor());

        factory.setPrefetchCount(prefetchCount.get());
        return factory;
    }

    @Bean
    public RetryOperationsInterceptor retryInterceptor() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffOptions(2000, 2.0, 10000)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
    }

    @Bean
    public Declarables rabbitMqBindings() {
        DirectExchange tasksExchange = new DirectExchange("tasks_exchange");
        DirectExchange tasksDlqExchange = new DirectExchange("tasks_dlq_exchange");

        Queue tasksQueue = QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", "tasks_dlq_exchange")
                .withArgument("x-dead-letter-routing-key", "tasks_dlq")
                .build();

        Queue priorityQueue = QueueBuilder.durable("priority_queue")
                .withArgument("x-max-priority", 10)
                .build();

        Queue tasksDlqQueue = QueueBuilder.durable("tasks_dlq").build();

        return new Declarables(
                tasksExchange,
                tasksDlqExchange,
                tasksQueue,
                priorityQueue,
                tasksDlqQueue,
                BindingBuilder.bind(tasksQueue).to(tasksExchange).with("tasks_routing"),
                BindingBuilder.bind(priorityQueue).to(tasksExchange).with("priority_routing"),
                BindingBuilder.bind(tasksDlqQueue).to(tasksDlqExchange).with("tasks_dlq")
        );
    }

    @Bean
    @Primary
    public RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry() {
        return new RabbitListenerEndpointRegistry();
    }

    public void updatePrefetchCount(int newCount) {
        prefetchCount.set(newCount);
    }

    public int getPrefetchCount() {
        return prefetchCount.get();
    }

}