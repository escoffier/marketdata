package com.stock.model;

import java.math.BigDecimal;
import java.util.Date;

public class TradeResponse {

    private String ticker;

    private long quantity;

    private BigDecimal price;

    private String orderType;

    private String confirmationNumber;

    private boolean error;

    private String errorMessage;

    private String accountName;

    private long timestamp = new Date().getTime();

    private String requestId;
}
