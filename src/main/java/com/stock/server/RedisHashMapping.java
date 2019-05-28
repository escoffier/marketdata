package com.stock.server;

import com.stock.model.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisHashMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisHashMapping.class);

//    @Autowired
//    @Qualifier("redisTemplate")
//    RedisTemplate<String, Object> redisTemplate;

    //@Autowired
    @Resource(name="redisTemplate")
    ///HashOperations<String, String, Object> hashOperations;
    HashOperations<String, String, Object> hashOperations;

    HashMapper<Object, String, Object> mapper = new Jackson2HashMapper(false);

    public void writeHash(String key, Quote quote) {
        Map<String, Object> mappedHash = mapper.toHash(quote);
        //LOGGER.info("Quote key: " + key);
        System.out.println("---------------reidis hash key: " + key);
        //hashOperations.putAll(key, mappedHash);
        hashOperations.put(Quote.OBJECT_KEY1, key, quote);
    }

    /**
     * @Author: Robbie
     * @Description: 通过pipeline写数据，数据格式为key: hashkey: joson-obj
     * @param quoteList
     * @Return: void
     * @Date: 下午5:09 19-1-8
     */
    public void pipelineWrite(List<Quote> quoteList) {

        //redisTemplate.executePipelined(new RedisCallback<Object>() {
        hashOperations.getOperations().executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                //LettuceConnection lettuceConnection = (LettuceConnection) connection;
                //lettuceConnection.hashCommands().hMSet();
                for (Quote quote : quoteList){
                    LOGGER.info("pipelineWrite: " + quote);
                    hashOperations.put(Quote.OBJECT_KEY1, quote.getId(), quote);
                }
                return null;
            }
        });
    }


    public void pipelineWrite1(List<Quote> quoteList) {

        //redisTemplate.executePipelined(new RedisCallback<Object>() {
        hashOperations.getOperations().executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (Quote quote : quoteList){
                    Map<String, Object> mappedHash = mapper.toHash(quote);
                    LOGGER.info("pipelineWrite1: " + quote.getId());
                    hashOperations.putAll(quote.getId(), mappedHash);
                    hashOperations.getOperations().expire(quote.getId(), 60, TimeUnit.SECONDS);
                    //redisTemplate.expire(quote.getId(), 30, TimeUnit.SECONDS);

                }
                return null;
            }
        });
    }

    public void pipelineWrite2(List<Quote> quoteList) {
        //List<Object> txResults = redisTemplate.executePipelined(new SessionCallback<Object>() {
        List<Object> txResults = hashOperations.getOperations().executePipelined(new SessionCallback<Object>() {
            @Override
            public  Object execute(RedisOperations operations) throws DataAccessException {
                //operations.multi();
                for (Quote quote : quoteList) {
                    LOGGER.info("pipelineWrite2: " + quote);
                    Map<String, Object> mappedHash = mapper.toHash(quote);
                    hashOperations.putAll(quote.getId(), mappedHash);

                    //operations.opsForHash().putAll(quote.getId(), mappedHash);
                }
                return null;
            }
        });
    }

    public Quote getHash(String key) {
        Map<String, Object> mappedHash =  hashOperations.entries(key);
        Quote quote = (Quote) mapper.fromHash(mappedHash);
        //quote.setPrice((String) mappedHash.get("price"));
        return quote;
    }

    public Quote getQuote1(String key) {
        try {
            Map<String, Object> mappedHash =  hashOperations.entries(key);
            Quote quote = (Quote) mapper.fromHash(mappedHash);
            return quote;

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quote Not Found");
        }

        //quote.setPrice((String) mappedHash.get("price"));
        //return quote;
    }


    public Quote getQuote(String key) {
        Quote quote = (Quote) hashOperations.get(Quote.OBJECT_KEY1, key);
        return quote;
    }
}
