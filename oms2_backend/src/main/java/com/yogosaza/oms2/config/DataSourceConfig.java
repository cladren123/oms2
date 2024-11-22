package com.yogosaza.oms2.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.yogosaza.oms2", // 리포지토리 패키지
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class DataSourceConfig {

    public static final String SOURCE_DATASOURCE = "sourceDataSource";
    public static final String REPLICA_DATASOURCE = "replicaDataSource";

    @Value("${spring.datasource.source.hikari.driver-class-name}")
    private String sourceDriverClassName;

    @Value("${spring.datasource.source.hikari.url}")
    private String sourceJdbcUrl;

    @Value("${spring.datasource.source.hikari.username}")
    private String sourceUsername;

    @Value("${spring.datasource.source.hikari.password}")
    private String sourcePassword;

    @Value("${spring.datasource.replica.hikari.driver-class-name}")
    private String replicaDriverClassName;

    @Value("${spring.datasource.replica.hikari.url}")
    private String replicaJdbcUrl;

    @Value("${spring.datasource.replica.hikari.username}")
    private String replicaUsername;

    @Value("${spring.datasource.replica.hikari.password}")
    private String replicaPassword;

    @Bean(SOURCE_DATASOURCE)
    public DataSource sourceDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(sourceDriverClassName)
                .url(sourceJdbcUrl)
                .username(sourceUsername)
                .password(sourcePassword)
                .build();
    }

    @Bean(REPLICA_DATASOURCE)
    public DataSource replicaDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(replicaDriverClassName)
                .url(replicaJdbcUrl)
                .username(replicaUsername)
                .password(replicaPassword)
                .build();
    }




    @Bean
    public DataSource routingDataSource(@Qualifier(SOURCE_DATASOURCE) DataSource sourceDataSource,
                                        @Qualifier(REPLICA_DATASOURCE) DataSource replicaDataSource) {

        RoutingDataSource routingDataSource = new RoutingDataSource(sourceDataSource, replicaDataSource);

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("source", sourceDataSource);
        dataSourceMap.put("replica", replicaDataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(sourceDataSource);

        return routingDataSource;
    }

    // 라우팅 데이터베이스를 기본 DataSource로 설정하는 빈 설정
    @Primary
    @Bean
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

}
