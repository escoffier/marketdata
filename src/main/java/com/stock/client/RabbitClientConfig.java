package com.stock.client;

import com.stock.AbstractStockAppRabbitConfiguration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitClientConfig extends AbstractStockAppRabbitConfiguration {

    @Value("${stocks.quote.pattern}")
    private String marketDataRoutingKey;

    @Override
    protected void configureRabbitTemplate(RabbitTemplate template) {
        template.setRoutingKey(STOCK_REQUEST_QUEUE_NAME);
    }


    @Bean
    Queue marketDataQueue() {
        return amqpAdmin().declareQueue();
    }

    @Bean
    Binding marketDataBinding() {
        return BindingBuilder.bind(marketDataQueue())
                .to(marketDataExchange())
                .with(marketDataRoutingKey);
    }

    @Bean
    DirectMessageListenerContainer directContainer(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        DirectMessageListenerContainer directMessageListenerContainer = new DirectMessageListenerContainer();
        directMessageListenerContainer.setConnectionFactory(connectionFactory);
        directMessageListenerContainer.setQueues(marketDataQueue(), traderJoeQueue());
        directMessageListenerContainer.setMessageListener(listenerAdapter);
        directMessageListenerContainer.setConsumersPerQueue(3);
        directMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return directMessageListenerContainer;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(MessageReceiver receiver){
        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver);
        adapter.setMessageConverter(new Jackson2JsonMessageConverter());
        adapter.setDefaultListenerMethod("receiveMessage");
        return adapter;
    }

    //trade response
    @Bean("traderJoeQueue")
    public Queue traderJoeQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public AmqpAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

}
