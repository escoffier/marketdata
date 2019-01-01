package com.stock.server;

import com.stock.AbstractStockAppRabbitConfiguration;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

@Configuration
public class RabbitServerConfig extends AbstractStockAppRabbitConfiguration {


    @Override
    protected void configureRabbitTemplate(RabbitTemplate template) {
        template.setMandatory(true);
        template.setReturnCallback( (Message message, int replyCode, String replyText,
                                     String exchange, String routingKey) -> {
            System.out.println("-----ReturnCallback: " + replyText);

        });

        template.setConfirmCallback((@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) ->{
            System.out.println("-----ConfirmCallback: " + ack + "   cause: " + cause);
        });
        template.setExchange(MARKET_DATA_EXCHANGE_NAME);
    }

    @Bean
    Queue stockRequestQueue() {
        return new Queue(STOCK_REQUEST_QUEUE_NAME);
    }

    @Bean
    DirectMessageListenerContainer directContainer(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        DirectMessageListenerContainer directMessageListenerContainer = new DirectMessageListenerContainer();
        directMessageListenerContainer.setConnectionFactory(connectionFactory);
        directMessageListenerContainer.setQueues(stockRequestQueue());
        directMessageListenerContainer.setMessageListener(listenerAdapter);
        directMessageListenerContainer.setConsumersPerQueue(3);
        return directMessageListenerContainer;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(ServerHandler serverHandler) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(serverHandler);
        adapter.setMessageConverter(new Jackson2JsonMessageConverter());
        adapter.setDefaultListenerMethod("handleMessage");
        return adapter;
    }

}
