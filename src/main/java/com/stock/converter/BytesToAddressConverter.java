package com.stock.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.model.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@ReadingConverter
public class BytesToAddressConverter implements Converter<byte[], Address> {

    private final Jackson2JsonRedisSerializer<Address> serializer;

    public BytesToAddressConverter() {
        serializer = new Jackson2JsonRedisSerializer<Address>(Address.class);
        serializer.setObjectMapper(new ObjectMapper());
    }

    @Override
    public Address convert(byte[] bytes) {
        return serializer.deserialize(bytes);
    }
}
