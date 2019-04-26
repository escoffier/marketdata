package com.stock.server;

import com.stock.repository.PersonRepository;
import com.stock.model.Person;
import com.stock.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class Controller {


    @Autowired
    RedisHashMapping redisHashMapping;
    //QuoteOperation quoteOperation;

    @Autowired
    PersonRepository repository;

    @GetMapping("/quote/{id}")
    Quote getQuote(@PathVariable("id") String id) {

        Quote quote = redisHashMapping.getQuote1(id);
        //Quote quote = quoteOperation.getHash(id);
        return quote;
    }

    @GetMapping("/person/{id}")
    Person getPerson(@PathVariable("id") String id) {
        return repository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
        //return person;
    }
}
