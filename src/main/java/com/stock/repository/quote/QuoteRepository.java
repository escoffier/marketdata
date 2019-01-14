package com.stock.repository.quote;

import com.stock.model.Quote;
import org.springframework.data.repository.CrudRepository;

public interface QuoteRepository extends CrudRepository<Quote, String> {
}
