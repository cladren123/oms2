package com.yogosaza.oms2.aop;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class MasterHealthIndicator implements HealthIndicator {

    private final DataSource masterDataSource;

    public MasterHealthIndicator(DataSource masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = masterDataSource.getConnection()) {
            // 연결 상태가 정상일 때 Health.up() 반환
            return Health.up().withDetail("Database", "Master DB is reachable").build();
        } catch (SQLException e) {
            // 연결이 불가능할 때 Health.down() 반환, 예외 메시지 포함
            return Health.down().withDetail("Error", e.getMessage()).build();
        }
    }
}
