package com.stock.repository.stockinfo;

import com.stock.model.StockInfo;
import org.springframework.data.repository.CrudRepository;

public interface StockInfoRepository  extends CrudRepository<StockInfo, Integer> {
}
