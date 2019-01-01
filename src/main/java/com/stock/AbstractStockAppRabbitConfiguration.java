package com.stock;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public abstract class AbstractStockAppRabbitConfiguration {

    protected static String MARKET_DATA_EXCHANGE_NAME = "stock.marketdata";//topic exchange的名稱

    protected static String STOCK_REQUEST_QUEUE_NAME = "stock.request";

    protected static String STOCK_REQUEST_ROUTING_KEY = STOCK_REQUEST_QUEUE_NAME;


    @Value("${amqp.port:5672}")
    private int port = 5672;

    @Bean
    public ConnectionFactory connectionFactory(){
        System.out.println("--------ConnectionFactory");
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("140.143.45.252");
        connectionFactory.setUsername("robbie");
        connectionFactory.setPassword("19811981");
        connectionFactory.setVirtualHost("orderqueue");
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }

    protected abstract void configureRabbitTemplate(RabbitTemplate template);

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(messageConverter());

        //retry policy
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500);
        backOffPolicy.setMultiplier(10.0);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        configureRabbitTemplate(rabbitTemplate);
        return  rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange marketDataExchange() {
        return new TopicExchange("stock.marketdata");
    }

    @Bean
    public RabbitAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
}
