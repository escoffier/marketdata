package com.stock.config;

import com.stock.model.Quote;
import com.stock.repository.quote.QuoteRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "quoteEntityManager",
        basePackages = {"com.stock.repository.quote"},
        transactionManagerRef = "quoteTransactionManager")
public class QuoteJpaConfig {

    @Bean(name = "quoteDataSource")
    @ConfigurationProperties(prefix = "app.quote.datasource")
    DataSource quoteDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "quoteEntityManager")
    LocalContainerEntityManagerFactoryBean quoteEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(quoteDataSource())
                .packages(Quote.class)
                .persistenceUnit("quote")
                .build();
    }

    @Bean(name = "quoteTransactionManager")
    public JpaTransactionManager transactionManager(@Qualifier("quoteEntityManager") EntityManagerFactory serversEntityManager){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(serversEntityManager);

        return transactionManager;
    }
}
