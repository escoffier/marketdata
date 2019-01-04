package com.stock.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Stock implements Serializable {

    private static final long serialVersionUID = 111L;

    private String ticker;

    private StockExchange stockExchange;

    public Stock() {
        this.ticker = null;
    }

    @JsonCreator
    public Stock(@JsonProperty(value = "ticker", required = true) String ticker,
                 @JsonProperty(value = "stockExchange", required = true) StockExchange stockExchange) {
        this.ticker = ticker;
        this.stockExchange = stockExchange;
    }


    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public StockExchange getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    @Override
    public String toString() {
        return "Stock [ticker=" + ticker + ", stockExchange=" + stockExchange + "]";
    }
}
