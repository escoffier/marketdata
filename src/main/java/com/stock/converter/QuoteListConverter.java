package com.stock.converter;

import com.stock.model.Quote;
import com.stock.model.QuoteList;
import com.stock.model.Stock;
import com.stock.model.StockExchange;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuoteListConverter implements HttpMessageConverter<QuoteList> {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteListConverter.class);
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        if (mediaType == null) {
            return false;
        }
        if (mediaType.includes(new MediaType("application","javascript"))) {
            return true;
        }

        return true;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(new MediaType("application", "javascript"));
    }

    @Override
    public QuoteList read(Class<? extends QuoteList> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        List<String>  lines = IOUtils.readLines(inputMessage.getBody(), inputMessage.getHeaders().getContentType().getCharset());

        List<Quote> quoteList = new ArrayList<>();
        for (String line : lines) {
            String stockCodeExchange = line.substring(line.indexOf("hq_str_") + 7, line.indexOf("="));

            StockExchange stockExchange = null;
            String stockExchangeStr = stockCodeExchange.substring(0, 2);
            if (stockExchangeStr.equals("sh") ) {
                stockExchange = StockExchange.shanghai;
            } else if (stockExchangeStr.equals("sz")) {
                stockExchange = StockExchange.shenzhen;
            } else {
                LOGGER.error("Invalid exchange: " + stockExchangeStr);
            }
            String tickers = stockCodeExchange.substring(2);

            int begin = line.indexOf("\"") + 1;
            String data = line.substring(begin, line.lastIndexOf("\""));
            LOGGER.trace("----body: " + line);

            //String tickers = data.substring(0, data.indexOf(","));
            String[] stringList = data.split(",");
            String price = stringList[3];
            Stock stock = new Stock(tickers, stockExchange);
            Quote quote = new Quote(stock, price);
            quoteList.add(quote);
            //return quote;

        }
        return new QuoteList(quoteList);
    }

    @Override
    public void write(QuoteList quoteList, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

    }
    //    @Override
//    public List<Quote> read(Class<? extends List<Quote>> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
//        List<String>  lines = IOUtils.readLines(inputMessage.getBody(), inputMessage.getHeaders().getContentType().getCharset());
//
//        List<Quote> quoteList = new ArrayList<>();
//        for (String line : lines) {
//            String stockCode = line.substring(line.indexOf("hq_str_") + 7, line.indexOf("="));
//            StockExchange stockExchange = null;
//            String stockExchangeStr = stockCode.substring(0, 2);
//            if (stockExchangeStr.equals("sh") ) {
//                stockExchange = StockExchange.shanghai;
//            } else if (stockExchangeStr.equals("sz")) {
//                stockExchange = StockExchange.shenzhen;
//            } else {
//                LOGGER.error("Invalid exchange: " + stockExchangeStr);
//
//            }
//
//            int begin = line.indexOf("\"") + 1;
//            String data = line.substring(begin, line.lastIndexOf("\""));
//            LOGGER.info("----body: " + line);
//
//
//            String tickers = data.substring(0, data.indexOf(","));
//            String[] stringList = data.split(",");
//            String price = stringList[3];
//            Stock stock = new Stock(tickers, stockExchange);
//            Quote quote = new Quote(stock, price);
//            quoteList.add(quote);
//            //return quote;
//
//        }
//        return quoteList;
//    }
//
//    @Override
//    public void write(List<Quote> quoteList, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
//
//    }
}
