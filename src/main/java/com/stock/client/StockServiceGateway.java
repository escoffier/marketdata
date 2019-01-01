package com.stock.client;

import com.stock.model.TradeRequest;

public interface StockServiceGateway {
    void send(TradeRequest request);
}
