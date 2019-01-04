package com.stock.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.MarketDataGateway;
import com.stock.repository.PersonRepository;
import com.stock.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.*;

@Component
public class RabbitMarketDataGateway implements MarketDataGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMarketDataGateway.class);

    private static final Random random = new Random();

    private final List<MockStock> stocks = new ArrayList<MockStock>();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisHashMapping redisHashMapping;
    //private QuoteOperation quoteOperation;


    @Autowired
    private PersonRepository repository;


    @Autowired
    private RestTemplate restTemplate;

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

    @Scheduled(fixedDelay = 10000)
    public void sendMarketData() {
        //Quote quote = generateFakeQuote();
        //Stock stock = quote.getStock();

        Quote quote = getQuote();
        Stock stock = quote.getStock();

        redisHashMapping.writeHash(quote.getId(), quote);
        //quoteOperation.writeHash(quote.getId(), quote);


        Person person = new Person(UUID.randomUUID().toString(), "qiu", "al'thor");
        LOGGER.info("Person id : " + person.getId());

        person.setAddress(new Address("emond's field", "andor"));
        repository.save(person);

        LOGGER.info("Sending Market Data for " + stock.getTicker());
        System.out.println("Sending Market Data for " + stock.getTicker());

        String routingKey = "stock.quotes." + stock.getStockExchange() + "." + stock.getTicker();
        //getRabbitOperations().convertAndSend(routingKey, quote);
        rabbitTemplate.convertAndSend(routingKey, quote);

    }

    private LinkedList<Quote> getQuotes() {
        String url = "http://hq.sinajs.cn/list=sh601003,sh601001";
        //String body = (String) restTemplate.getForObject(url, String.class);

        ResponseEntity<Quote> entity = restTemplate.getForEntity(url, Quote.class);

        Quote quote = entity.getBody();
        LOGGER.info("data: " + quote);
        return new LinkedList<>();
    }

    private Quote getQuote() {
        String url = "http://hq.sinajs.cn/list=sh601001";
        //String body = (String) restTemplate.getForObject(url, String.class);
        ResponseEntity<Quote> entity = restTemplate.getForEntity(url, Quote.class);
        Quote quote = entity.getBody();
        LOGGER.info("restTemplate data: " + quote);
        return quote;
    }

    private Quote generateFakeQuote() {
        MockStock stock = this.stocks.get(random.nextInt(this.stocks.size()));
        String price = stock.randomPrice();
        Quote quote = new Quote(stock, price);
        quote.setId(UUID.randomUUID().toString());
        return quote;
    }

    private static class MockStock extends Stock {

        @JsonIgnore
        private final int basePrice;

        private final DecimalFormat twoPlacesFormat = new DecimalFormat("0.00");

        private MockStock(String ticker, StockExchange stockExchange, int basePrice) {
            super(ticker , stockExchange);
            this.basePrice = basePrice;
        }

        private MockStock(@JsonProperty(value = "ticker", required = true) String ticker,
                          @JsonProperty(value = "stockExchange", required = true) StockExchange stockExchange) {
            super(ticker , stockExchange);
            this.basePrice = 0;
        }

        private String randomPrice() {
            return this.twoPlacesFormat.format(this.basePrice + Math.abs(random.nextGaussian()));
        }
    }

}
