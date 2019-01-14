package com.stock.config;

import com.stock.model.StockInfo;
import com.stock.repository.stockinfo.StockInfoRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "stockInfoManager",
        basePackages = {"com.stock.repository.stockinfo"},
         transactionManagerRef = "stockInfoTransactionManager")
public class StockInfoJpaConfig {

    @Bean(name = "stockInfoDataSource")
    @Primary
    @ConfigurationProperties(prefix = "app.datasource")
    DataSource stockInfoDataSource(){
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "stockInfoManager")
    @Primary
    LocalContainerEntityManagerFactoryBean stockInfoManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("stockInfoDataSource") DataSource stockInfoDataSource) {
        return builder.dataSource(stockInfoDataSource)
                .packages(StockInfo.class)
                .persistenceUnit("stockInfo")
                .build();
    }

    @Bean(name = "stockInfoTransactionManager")
    public JpaTransactionManager transactionManager(@Qualifier("stockInfoManager") EntityManagerFactory serversEntityManager){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(serversEntityManager);

        return transactionManager;
    }
}
