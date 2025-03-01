package com.demo.producer.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.PooledChannelConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    Dotenv dotenv = Dotenv.load();
    public static final String QUEUE_NAME = "queue.payment";
    public static final String EXCHANGE_NAME = "payment";
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue requestQueue, TopicExchange exchange) {
        return BindingBuilder.bind(requestQueue).to(exchange).with("payment.create");
    }
    @Bean
    public PooledChannelConnectionFactory pooledChannelConnectionFactory() {
        com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory = new com.rabbitmq.client.ConnectionFactory();
        rabbitConnectionFactory.setHost(dotenv.get("RABBITMQ_HOST"));
        rabbitConnectionFactory.setPort(Integer.parseInt(dotenv.get("RABBITMQ_PORT")));
        rabbitConnectionFactory.setUsername(dotenv.get("RABBITMQ_USERNAME"));
        rabbitConnectionFactory.setPassword(dotenv.get("RABBITMQ_PASSWORD"));
        rabbitConnectionFactory.setVirtualHost(dotenv.get("RABBITMQ_VIRTUAL_HOST"));
        PooledChannelConnectionFactory pooledChannelConnectionFactory =  new PooledChannelConnectionFactory(rabbitConnectionFactory);
        pooledChannelConnectionFactory.setPoolConfigurer((pool, tx) -> {
            if (tx) {
                pool.setMaxTotal(Integer.parseInt(dotenv.get("RABBITMQ_MAX_TOTAL")));
                pool.setMinIdle(Integer.parseInt(dotenv.get("RABBITMQ_MIN_IDLE")));
                pool.setTestOnBorrow(true);
                pool.setTestWhileIdle(true);
            }
            else {
                pool.setMaxTotal(Integer.parseInt(dotenv.get("RABBITMQ_MAX_TOTAL")));
                pool.setMinIdle(Integer.parseInt(dotenv.get("RABBITMQ_MIN_IDLE")));
                pool.setTestOnBorrow(true);
                pool.setTestWhileIdle(true);
            }
        });
        return pooledChannelConnectionFactory;
    }
    @Bean
    public RabbitTemplate rabbitTemplate(PooledChannelConnectionFactory pooledChannelConnectionFactory) {
        return new RabbitTemplate(pooledChannelConnectionFactory);
    }
}
