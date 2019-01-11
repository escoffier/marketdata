package com.stock.client;

import com.stock.model.TradeRequest;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RabbitStockServiceGateway implements StockServiceGateway {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private String defaultReplyTo;

    public void setDefaultReplyTo(String defaultReplyTo) {
        this.defaultReplyTo = defaultReplyTo;
    }

    @Override
    public void send(TradeRequest request) {

        rabbitTemplate.convertAndSend(request, message -> {
            message.getMessageProperties().setReplyTo(defaultReplyTo);
            message.getMessageProperties().setCorrelationId(UUID.randomUUID().toString());
            return message;
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                message.getMessageProperties().setReplyTo(defaultReplyTo);
//                message.getMessageProperties().setCorrelationId(UUID.randomUUID().toString());
//                return message;
//            }
        });

    }
}
