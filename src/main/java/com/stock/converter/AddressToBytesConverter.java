package com.stock.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.model.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@WritingConverter
public class AddressToBytesConverter implements Converter<Address, byte[]> {

    private final Jackson2JsonRedisSerializer<Address> serializer;

    public AddressToBytesConverter() {
        serializer = new Jackson2JsonRedisSerializer<Address>(Address.class);
        serializer.setObjectMapper(new ObjectMapper());
    }

    @Override
    public byte[] convert(Address address) {
        return serializer.serialize(address);
    }
}
