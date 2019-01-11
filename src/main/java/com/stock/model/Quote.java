package com.stock.model;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "quote")
public class Quote implements Serializable {

    private static final long serialVersionUID = 10001L;

    public static final String  OBJECT_KEY = "quote_key";
    public static final String  OBJECT_KEY1 = "quote_key1";

    @Id
    private String id;

    private String price;

    private long timestamp;

    @Embedded
    private Stock stock;

    @Transient
    private DateFormat format = DateFormat.getTimeInstance();

    public Quote() {
        this(null, null);
    }

    public Quote(String id, Stock stock, String price, long timestamp) {
        this.id = id;
        this.stock = stock;
        this.price = price;
        this.timestamp = timestamp;
    }

    public Quote(Stock stock, String price) {
        this.stock = stock;
        this.price = price;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    private String getTimeString() {
        return format.format(new Date(timestamp));
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Quote [id = " + id +  ", time=" + getTimeString() + ", stock=" + stock + ", price=" + price + "]";
    }
}
