package com.stock.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Embeddable
public class Stock implements Serializable {

    private static final long serialVersionUID = 111L;

    private String ticker;

    @Enumerated(EnumType.STRING)
    @Column(name = "exchange")
    private StockExchange exchange;

    public Stock() {
        this.ticker = null;
    }

    @JsonCreator
    public Stock(@JsonProperty(value = "ticker", required = true) String ticker,
                 @JsonProperty(value = "stockExchange", required = true) StockExchange stockExchange) {
        this.ticker = ticker;
        this.exchange = stockExchange;
    }


    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public StockExchange getStockExchange() {
        return exchange;
    }

    public void setStockExchange(StockExchange stockExchange) {
        this.exchange = stockExchange;
    }

    @Override
    public String toString() {
        return "Stock [ticker=" + ticker + ", stockExchange=" + exchange + "]";
    }
}
