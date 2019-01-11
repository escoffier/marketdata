package com.stock.server;

import com.stock.converter.AddressToBytesConverter;
import com.stock.converter.BytesToAddressConverter;
import com.stock.converter.QuoteConverter;
import com.stock.converter.QuoteListConverter;
import com.stock.model.Quote;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


@Configuration
@EnableRedisRepositories(basePackages = {"com.stock.repository", "com.stock.converter"})
public class RedisConfig {
    @Bean
    public LettuceConnectionFactory reidsConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("192.168.21.225", 6379));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(reidsConnectionFactory());

        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        template.setHashKeySerializer(jsonRedisSerializer);
        template.setKeySerializer(new StringRedisSerializer());

        template.setHashValueSerializer(jsonRedisSerializer);
        template.setValueSerializer(jsonRedisSerializer);
        //template.expire()
        return template;
    }

    @Bean
    public RedisCustomConversions redisCustomConversions(){
        return new RedisCustomConversions(Arrays.asList(new AddressToBytesConverter(), new BytesToAddressConverter()));
    }

//    @Bean("redisTemplate")
//    public RedisTemplate<String, Quote> quoteRedisTemplate() {
//        RedisTemplate<String, Quote> template = new RedisTemplate<>();
//        template.setConnectionFactory(reidsConnectionFactory());
//
//        Jackson2JsonRedisSerializer<Quote> jsonRedisSerializer = new Jackson2JsonRedisSerializer<Quote>(Quote.class);
//        template.setHashKeySerializer(jsonRedisSerializer);
//        template.setKeySerializer(new StringRedisSerializer());
//
//
//        template.setHashValueSerializer(jsonRedisSerializer);
//        template.setValueSerializer(jsonRedisSerializer);
//        return template;
//    }

//    @Bean(name = "redisTemplate")
//    public <String,V> RedisTemplate<String,V> getRedisTemplate(){
//        RedisTemplate<String,V> redisTemplate =  new RedisTemplate<String, V>();
//        redisTemplate.setConnectionFactory(reidsConnectionFactory());
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        return redisTemplate;
//    }

//    @Bean(name = "repoRedisTemplate")
//    public RedisTemplate<?, ?> repoRedisTemplate() {
//
//        RedisTemplate<byte[], byte[]> template = new RedisTemplate<byte[], byte[]>();
//        return template;
//    }
    //@Resource()

    @Bean
    //public RestTemplate restTemplate(RestTemplateBuilder builder) {
    public RestTemplate restTemplate() {
        //MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.getSupportedMediaTypes().add(new MediaType("application","javascript"));

        //RestTemplate restTemplate = builder.build();
        //restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter());
        //return builder.build();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(quoteListConverter());
        return restTemplate;
    }

    //@Bean
    public QuoteConverter jsQuoteConverter() {
        QuoteConverter quoteConverter = new QuoteConverter();
        return quoteConverter;
    }

    public QuoteListConverter quoteListConverter() {
        return new QuoteListConverter();
    }

    //@Bean
//    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
//        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
//        jsonConverter.getSupportedMediaTypes().add(new MediaType("application","javascript"));
//
//        return jsonConverter;
//    }
}
