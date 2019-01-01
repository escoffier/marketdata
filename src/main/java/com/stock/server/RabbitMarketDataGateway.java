package com.stock.server;

import com.stock.MarketDataGateway;
import com.stock.model.Quote;
import com.stock.model.Stock;
import com.stock.model.StockExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class RabbitMarketDataGateway implements MarketDataGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMarketDataGateway.class);

    private static final Random random = new Random();

    private final List<MockStock> stocks = new ArrayList<MockStock>();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public RabbitMarketDataGateway() {
        this.stocks.add(new MockStock("AAPL", StockExchange.shanghai, 255));
        this.stocks.add(new MockStock("CSCO", StockExchange.shanghai, 22));
        this.stocks.add(new MockStock("DELL", StockExchange.shanghai, 15));
        this.stocks.add(new MockStock("GOOG", StockExchange.shanghai, 500));
        this.stocks.add(new MockStock("INTC", StockExchange.shanghai, 22));
        this.stocks.add(new MockStock("MSFT", StockExchange.shanghai, 29));
        this.stocks.add(new MockStock("ORCL", StockExchange.shanghai, 24));
        this.stocks.add(new MockStock("CAJ", StockExchange.shenzhen, 43));
        this.stocks.add(new MockStock("F", StockExchange.shenzhen, 12));
        this.stocks.add(new MockStock("GE", StockExchange.shenzhen, 18));
        this.stocks.add(new MockStock("HMC", StockExchange.shenzhen, 32));
        this.stocks.add(new MockStock("HPQ", StockExchange.shenzhen, 48));
        this.stocks.add(new MockStock("IBM", StockExchange.shenzhen, 130));
        this.stocks.add(new MockStock("TM", StockExchange.shenzhen, 76));
    }

    @Scheduled(fixedDelay = 1000)
    public void sendMarketData() {
        Quote quote = generateFakeQuote();
        Stock stock = quote.getStock();
        LOGGER.info("Sending Market Data for " + stock.getTicker());
        System.out.println("Sending Market Data for " + stock.getTicker());

        String routingKey = "stock.quotes."  + stock.getStockExchange() + "." + stock.getTicker();
        //getRabbitOperations().convertAndSend(routingKey, quote);
        rabbitTemplate.convertAndSend(routingKey, quote);

    }

    private Quote generateFakeQuote() {
        MockStock stock = this.stocks.get(random.nextInt(this.stocks.size()));
        String price = stock.randomPrice();
        return new Quote(stock, price);
    }

    private static class MockStock extends Stock {

        private final int basePrice;
        private final DecimalFormat twoPlacesFormat = new DecimalFormat("0.00");

        private MockStock(String ticker, StockExchange stockExchange, int basePrice) {
            super(ticker , stockExchange);
            this.basePrice = basePrice;
        }

        private String randomPrice() {
            return this.twoPlacesFormat.format(this.basePrice + Math.abs(random.nextGaussian()));
        }
    }

}
