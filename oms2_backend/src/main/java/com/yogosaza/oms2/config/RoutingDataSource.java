package com.yogosaza.oms2.config;

import com.yogosaza.oms2.aop.SourceHealthIndicator;
import com.yogosaza.oms2.aop.ReplicaHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;


public class RoutingDataSource extends AbstractRoutingDataSource {

    private final DataSource sourceDataSource;
    private final DataSource replicaDataSource;

    public RoutingDataSource(DataSource sourceDataSource, DataSource replicaDataSource) {
        this.sourceDataSource = sourceDataSource;
        this.replicaDataSource = replicaDataSource;
    }

    @Override
    protected Object determineCurrentLookupKey() {

        // 여기서 현재 데이터 소스가 source인지 replica인지 결정
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        SourceHealthIndicator sourceHealthIndicator = new SourceHealthIndicator(sourceDataSource);
        ReplicaHealthIndicator replicaHealthIndicator = new ReplicaHealthIndicator(replicaDataSource);

        Health sourceHealth = sourceHealthIndicator.health();
        Health replicaHealth = replicaHealthIndicator.health();

        if (isReadOnly) {
            if (replicaHealth.getStatus().equals(Status.UP)) {
                return "replica";
            } else if (sourceHealth.getStatus().equals(Status.UP)) {
                return "source";
            } else {
                throw new IllegalStateException("No available data source");
            }
        } else {
            if (sourceHealth.getStatus().equals(Status.UP)) {
                return "source";
            } else if (replicaHealth.getStatus().equals(Status.UP)) {
                return "replica";
            } else {
                throw new IllegalStateException("Source data source is unavailable");
            }
        }

    }
}
