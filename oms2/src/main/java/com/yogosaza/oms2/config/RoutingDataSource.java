package com.yogosaza.oms2.config;

import com.yogosaza.oms2.aop.MasterHealthIndicator;
import com.yogosaza.oms2.aop.SlaveHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;


public class RoutingDataSource extends AbstractRoutingDataSource {

    private final DataSource masterDataSource;
    private final DataSource slaveDataSource;

    public RoutingDataSource(DataSource masterDataSource, DataSource slaveDataSource) {
        this.masterDataSource = masterDataSource;
        this.slaveDataSource = slaveDataSource;
    }

    @Override
    protected Object determineCurrentLookupKey() {

        // 여기서 현재 데이터 소스가 master인지 slave인지 결정
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        MasterHealthIndicator masterHealthIndicator = new MasterHealthIndicator(masterDataSource);
        SlaveHealthIndicator slaveHealthIndicator = new SlaveHealthIndicator(slaveDataSource);

        Health masterHealth = masterHealthIndicator.health();
        Health slaveHealth = slaveHealthIndicator.health();

        if (isReadOnly) {
            if (slaveHealth.getStatus().equals(Status.UP)) {
                return "slave";
            } else if (masterHealth.getStatus().equals(Status.UP)) {
                return "master";
            } else {
                throw new IllegalStateException("No available data source");
            }
        } else {
            if (masterHealth.getStatus().equals(Status.UP)) {
                return "master";
            } else if (slaveHealth.getStatus().equals(Status.UP)) {
                return "slave";
            } else {
                throw new IllegalStateException("Master data source is unavailable");
            }
        }

    }
}
