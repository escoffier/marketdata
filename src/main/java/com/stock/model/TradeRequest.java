package com.stock.model;

import java.math.BigDecimal;
import java.util.UUID;

public class TradeRequest {

    private String ticker;

    private long quantity;

    private BigDecimal price;

    private String orderType;

    private String accountName;

    private boolean buyRequest;

    private String userName;

    private String requestId;

    private String id = UUID.randomUUID().toString();
}
