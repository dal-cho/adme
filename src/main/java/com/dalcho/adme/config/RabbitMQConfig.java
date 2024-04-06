package com.dalcho.adme.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.connect.queue}")
    private String connectQueue;

    @Value("${rabbitmq.send.queue}")
    private String sendQueue;

    @Value("${rabbitmq.disconnect.queue}")
    private String disconnectQueue;

    @Value("${rabbitmq.connect.exchange}")
    private String connectExchange;

    @Value("${rabbitmq.send.exchange}")
    private String sendExchange;

    @Value("${rabbitmq.disconnect.exchange}")
    private String disconnectExchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Bean
    public Queue connectQueue() {
        return new Queue(connectQueue, true);
    }

    @Bean
    public Queue sendQueue() {
        return new Queue(sendQueue, true);
    }

    @Bean
    public Queue disconnectQueue() {
        return new Queue(disconnectQueue, true);
    }

    @Bean
    public TopicExchange connectExchange() {
        return new TopicExchange(connectExchange);
    }

    @Bean
    public TopicExchange sendExchange() {
        return new TopicExchange(sendExchange);
    }

    @Bean
    public TopicExchange disconnectExchange() {
        return new TopicExchange(disconnectExchange);
    }

    @Bean
    public Binding connectBinding() {
        return BindingBuilder
                .bind(connectQueue())
                .to(connectExchange())
                .with(routingKey);
    }

    @Bean
    public Binding sendBinding() {
        return BindingBuilder
                .bind(sendQueue())
                .to(sendExchange())
                .with(routingKey);
    }

    @Bean
    public Binding disconnectBinding() {
        return BindingBuilder
                .bind(disconnectQueue())
                .to(disconnectExchange())
                .with(routingKey);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        rabbitTemplate.setRoutingKey(routingKey);
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }
}