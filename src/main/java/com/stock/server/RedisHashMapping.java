package com.stock.server;

import com.stock.model.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class RedisHashMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisHashMapping.class);

    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate<String, Object> redisTemplate;

    //@Autowired
    @Resource(name="redisTemplate")
    //HashOperations<String, String, Object> hashOperations;
    HashOperations<String, String, Object> hashOperations;

    HashMapper<Object, String, Object> mapper = new Jackson2HashMapper(false);

    public void writeHash(String key, Quote quote) {
        //Map<String, Object> mappedHash = mapper.toHash(quote);
        //LOGGER.info("Quote key: " + key);
        System.out.println("---------------reidis hash key: " + key);
        //hashOperations.putAll(key, mappedHash);
        hashOperations.put(Quote.OBJECT_KEY1, key, quote);
    }

    public Quote getHash(String key) {
        Map<String, Object> mappedHash =  hashOperations.entries(key);
        Quote quote = (Quote) mapper.fromHash(mappedHash);
        //quote.setPrice((String) mappedHash.get("price"));
        return quote;
    }


    public Quote getQuote(String key) {
        Quote quote = (Quote) hashOperations.get(Quote.OBJECT_KEY1, key);
        return quote;
    }
}
