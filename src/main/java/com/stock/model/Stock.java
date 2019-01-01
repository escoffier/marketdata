package com.stock.model;

public class Stock {

    private String ticker;

    private StockExchange stockExchange;

    public Stock() {

    }

    public Stock(String ticker, StockExchange stockExchange) {
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
