package com.stock.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = {"com.stock.client", "com.stock.model"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.stock\\.server\\..*")})
public class ConsumerApp {

    public static void main(String[] args) {

        System.setProperty("server.port", "9081");

        SpringApplication.run(ConsumerApp.class, args);
    }

}
