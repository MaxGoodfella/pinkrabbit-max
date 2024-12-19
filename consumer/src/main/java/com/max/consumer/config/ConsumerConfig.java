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

/**
 * Конфигурация потребителя для работы с RabbitMQ.
 * <p>Этот класс настраивает контейнеры для прослушивания очередей, настройку повторных попыток (retry) и
 * биндинги для очередей RabbitMQ.</p>
 */
@Configuration
public class ConsumerConfig {

    @Value("${RABBITMQ_QUEUE_NAME}")
    private String queueName;

//    @Value("${RABBITMQ_PRIORITY_QUEUE_NAME}")
//    private String priorityQueueName;

    private final AtomicInteger prefetchCount = new AtomicInteger(10);

    /**
     * Создает {@link SimpleRabbitListenerContainerFactory} для настройки контейнера прослушивания RabbitMQ.
     * <p>Конфигурируется соединение, количество сообщений на одно соединение и
     * поведение при ошибке (повторные попытки).</p>
     */
    @Bean
    public SimpleRabbitListenerContainerFactory retryContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        factory.setAdviceChain(retryInterceptor());

        factory.setPrefetchCount(prefetchCount.get());
        return factory;
    }

    /**
     * Создает {@link RetryOperationsInterceptor} для настройки повторных попыток.
     * <p>Этот бин конфигурирует поведение при неудачной обработке сообщения
     * (максимальное количество попыток, задержка и стратегия восстановления).</p>
     */
    @Bean
    public RetryOperationsInterceptor retryInterceptor() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffOptions(2000, 2.0, 10000)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
    }

    /**
     * Создает биндинги для очередей и обменников RabbitMQ.
     * <p>Конфигурирует обменники и очереди для задач и Dead Letter Queue.</p>
     */
    @Bean
    public Declarables rabbitMqBindings() {
        DirectExchange tasksExchange = new DirectExchange("tasks_exchange");
        DirectExchange tasksDlqExchange = new DirectExchange("tasks_dlq_exchange");
//        DirectExchange tasksPriorityExchange = new DirectExchange("tasks_priority_exchange");

        Queue tasksQueue = QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", "tasks_dlq_exchange")
                .withArgument("x-dead-letter-routing-key", "tasks_dlq")
                .build();

        Queue tasksDlqQueue = QueueBuilder.durable("tasks_dlq").build();

//        Queue priorityQueue = QueueBuilder.durable(priorityQueueName)
//                .withArgument("x-max-priority", 10)
//                .build();

        return new Declarables(
                tasksExchange,
                tasksQueue,
                tasksDlqExchange,
                tasksDlqQueue,
//                tasksPriorityExchange,
//                priorityQueue,
                BindingBuilder.bind(tasksQueue).to(tasksExchange).with("tasks_key"),
                BindingBuilder.bind(tasksDlqQueue).to(tasksDlqExchange).with("tasks_dlq")
//                ,
//                BindingBuilder.bind(priorityQueue).to(tasksPriorityExchange).with("priority_key")
        );
    }

    /**
     * Создает {@link RabbitListenerEndpointRegistry} для регистрации и управления RabbitListener.
     * <p>Используется для управления конечными точками слушателей, которые обрабатывают сообщения из RabbitMQ.</p>
     */
    @Bean
    @Primary
    public RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry() {
        return new RabbitListenerEndpointRegistry();
    }


    /**
     * Обновляет количество сообщений, которое потребитель может забрать за один раз.
     */
    public void updatePrefetchCount(int newCount) {
        prefetchCount.set(newCount);
    }

    /**
     * Получает текущее количество сообщений, которое потребитель может забрать за один раз.
     */
    public int getPrefetchCount() {
        return prefetchCount.get();
    }

}