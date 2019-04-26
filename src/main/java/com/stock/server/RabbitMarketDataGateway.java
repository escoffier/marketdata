package com.stock.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.MarketDataGateway;
import com.stock.repository.PersonRepository;
import com.stock.model.*;
import com.stock.repository.quote.QuoteRepository;
import com.stock.repository.stockinfo.StockInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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

    private Iterable<StockInfo> stockInfoIterable;

    private List<String> stockNoList = new ArrayList<>();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisHashMapping redisHashMapping;
    //private QuoteOperation quoteOperation;


    @Autowired
    private PersonRepository repository;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private  StockInfoRepository stockInfoRepository;

    @Autowired
    private QuoteRepository quoteRepository;

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

//        Quote quote = getQuote();
//        Stock stock = quote.getStock();
//
//        redisHashMapping.writeHash(quote.getId(), quote);
//
//        String routingKey = "stock.quotes." + stock.getStockExchange() + "." + stock.getTicker();
//        rabbitTemplate.convertAndSend(routingKey, quote);

        List<Quote> quoteList = getQuoteList();
        redisHashMapping.pipelineWrite1(quoteList);
        for (Quote quote : quoteList) {
            Stock stock = quote.getStock();

            quoteRepository.save(quote);
            String routingKey = "stock.quotes." + stock.getStockExchange() + "." + stock.getTicker();
            //rabbitTemplate.convertAndSend(routingKey, quote);
        }

    }

    private void cachePerson() {
        Person person = new Person(UUID.randomUUID().toString(), "qiu", "al'thor");
        LOGGER.info("Person id : " + person.getId());

        person.setAddress(new Address("emond's field", "andor"));
        repository.save(person);
    }

    private List<Quote> getQuotes() {

        if (!stockInfoIterable.iterator().hasNext()) {
            stockInfoIterable = stockInfoRepository.findAll();
        }

        StringBuffer url = new StringBuffer("http://hq.sinajs.cn/list=sh601003,sh601001");

//        stockInfoIterable.forEach(new Consumer<StockInfo>() {
//            @Override
//            public void accept(StockInfo stockInfo) {
//                url.append(",sh").append(stockInfo.getStockNo());
//
//            }
//        });

        //String url = "http://hq.sinajs.cn/list=sh601003,sh601001";
        //String body = (String) restTemplate.getForObject(url, String.class);

        //ResponseEntity<List<Quote>> entity = restTemplate.getForEntity(url, new ParameterizedTypeReference<List<Quote>>(){});
        ResponseEntity<List<Quote>> entity =
                restTemplate.exchange(url.toString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Quote>>(){});

        List<Quote> quoteList = entity.getBody();
        LOGGER.info("data: " + quoteList);
        return quoteList;
    }

    private List<Quote> getQuoteList() {

        if (stockInfoIterable == null) {
            stockInfoIterable = stockInfoRepository.findAll();
            StringBuffer buffer = new StringBuffer(256);
            int index = 0;
            Iterator<StockInfo> iterable =  stockInfoIterable.iterator();
            while (iterable.hasNext()) {
                buffer.append(",sh").append(iterable.next().getStockNo());
                index++;


                if (index%10 == 0) {
                    stockNoList.add(buffer.toString());
                    buffer.delete(0, buffer.length());
                    //break ;
                }

                if (index >=100) {
                    break;
                }
            }

            if (buffer.length() > 0) {
                stockNoList.add(buffer.toString());
            }
        }

        QuoteList quoteList = new QuoteList();

        for (String st : stockNoList) {
            StringBuffer url = new StringBuffer("http://hq.sinajs.cn/list=");

            url.append(st);

            //LOGGER.info(" url: " + url);

            ResponseEntity<QuoteList> entity =
                    //restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Quote>>(){});
                    restTemplate.getForEntity(url.toString(), QuoteList.class);

             quoteList.append(entity.getBody());
        }


        LOGGER.info("data: " + quoteList);
        return quoteList.getQuoteList();
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
