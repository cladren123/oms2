package com.yogosaza.oms2.config;


import com.yogosaza.oms2.aop.SlaveHealthIndicator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

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

    public static final String MASTER_DATASOURCE = "masterDataSource";
    public static final String SLAVE_DATASOURCE = "slaveDataSource";

    @Value("${spring.datasource.master.hikari.driver-class-name}")
    private String masterDriverClassName;

    @Value("${spring.datasource.master.hikari.url}")
    private String masterJdbcUrl;

    @Value("${spring.datasource.master.hikari.username}")
    private String masterUsername;

    @Value("${spring.datasource.master.hikari.password}")
    private String masterPassword;

    @Value("${spring.datasource.slave.hikari.driver-class-name}")
    private String slaveDriverClassName;

    @Value("${spring.datasource.slave.hikari.url}")
    private String slaveJdbcUrl;

    @Value("${spring.datasource.slave.hikari.username}")
    private String slaveUsername;

    @Value("${spring.datasource.slave.hikari.password}")
    private String slavePassword;

    @Bean(MASTER_DATASOURCE)
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(masterDriverClassName)
                .url(masterJdbcUrl)
                .username(masterUsername)
                .password(masterPassword)
                .build();
    }

    @Bean(SLAVE_DATASOURCE)
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(slaveDriverClassName)
                .url(slaveJdbcUrl)
                .username(slaveUsername)
                .password(slavePassword)
                .build();
    }




    @Bean
    public DataSource routingDataSource(@Qualifier(MASTER_DATASOURCE) DataSource masterDataSource,
                                        @Qualifier(SLAVE_DATASOURCE) DataSource slaveDataSource) {

        RoutingDataSource routingDataSource = new RoutingDataSource(masterDataSource, slaveDataSource);

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("master", masterDataSource);
        dataSourceMap.put("slave", slaveDataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    // 라우팅 데이터베이스를 기본 DataSource로 설정하는 빈 설정
    @Primary
    @Bean
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

}
