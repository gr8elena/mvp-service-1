package com.example.mvp_service_1.config.multitenant;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    private DataSource createTenantDataSource(TenantDataSourceProperties props) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(props.getUrl());
        ds.setUsername(props.getUsername());
        ds.setPassword(props.getPassword());
        ds.setDriverClassName(props.getDriverClassName());
        return ds;
    }

    @Bean
    public TenantRoutingDataSource routingDataSource(
            @Qualifier("tenant1DataSourceProps") TenantDataSourceProperties tenant1,
            @Qualifier("tenant2DataSourceProps") TenantDataSourceProperties tenant2) {

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("tenant1", createTenantDataSource(tenant1));
        targetDataSources.put("tenant2", createTenantDataSource(tenant2));

        TenantRoutingDataSource routing = new TenantRoutingDataSource();
        routing.setTargetDataSources(targetDataSources);
        routing.setDefaultTargetDataSource(targetDataSources.get("tenant1"));
        routing.afterPropertiesSet();
        return routing;
    }

    @Bean
    @Primary
    public DataSource dataSource(TenantRoutingDataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.mvp_service_1.model")
                .persistenceUnit("multiTenantPU")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    @Bean
    @ConfigurationProperties(prefix = "tenant.datasource.tenant1")
    @Qualifier("tenant1DataSourceProps")
    public TenantDataSourceProperties tenant1DataSourceProps() {
        return new TenantDataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "tenant.datasource.tenant2")
    @Qualifier("tenant2DataSourceProps")
    public TenantDataSourceProperties tenant2DataSourceProps() {
        return new TenantDataSourceProperties();
    }

}