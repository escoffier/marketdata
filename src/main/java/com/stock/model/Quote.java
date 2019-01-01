package com.stock.model;

import java.text.DateFormat;
import java.util.Date;

public class Quote {
    private Stock stock;
    private String price;
    private long timestamp;

    private DateFormat format = DateFormat.getTimeInstance();

    public Quote() {
        this(null, null);
    }

    public Quote(Stock stock, String price) {
        this.stock = stock;
        this.price = price;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getTimeString() {
        return format.format(new Date(timestamp));
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Quote [time=" + getTimeString() + ", stock=" + stock + ", price=" + price + "]";

    }
}
