package com.stock.client.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class TradeJpaConfig {

    @Bean(name = "tradeDataSource")
    //@ConfigurationProperties(prefix = "app.trade.datasource")
    DataSource quoteDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://192.168.21.225:3306/trades?useSSL=true")
                .username("testuser")
                .password("19811981")
                .build();
    }
}
