package com.batch.multidb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Properties {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public PropertiesType.HikariProperty hikariProperty () {
        return new PropertiesType.HikariProperty();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.main-db")
    public PropertiesType.DbProperty mainProperty() {
        return new PropertiesType.DbProperty();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.shard-db")
    public List<PropertiesType.DbProperty> shardProperties() {
        return new ArrayList<>();
    }
}
