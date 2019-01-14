package com.stock.client;

import com.stock.model.TradeRequest;
import com.stock.model.TradeResponse;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/")
public class Controller {


    RabbitStockServiceGateway rabbitStockServiceGateway;

    @Autowired
    void setRabbitStockServiceGateway(RabbitStockServiceGateway rabbitStockServiceGateway, @Qualifier("traderJoeQueue")Queue queue) {
        this.rabbitStockServiceGateway = rabbitStockServiceGateway;
        this.rabbitStockServiceGateway.setDefaultReplyTo(queue.getName());
    }

    @PostMapping("/trade")
    void trade(@RequestBody TradeRequest request) {
        rabbitStockServiceGateway.send(request);
    }

}
