package com.lsnju.tpbase.daemon.monitor;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.metadata.HikariDataSourcePoolMetadata;

import com.zaxxer.hikari.HikariDataSource;

import lombok.Setter;

/**
 *
 * @author lisong
 * @since 2022/1/21 11:03
 * @version V1.0
 */
public class HikariCpMonitorTask extends AbstractHikariCpMonitorTask {

    @Setter
    @Autowired
    private HikariDataSource dataSource;

    @PostConstruct
    public void setup() {
        Objects.requireNonNull(dataSource);
        log.debug("{}", dataSource);
        log.debug("{}", new HikariDataSourcePoolMetadata(dataSource));
    }

    @Override
    protected void execute() {
        log.debug("HikariCpMonitorTask.execute");
        logInfo(dataSource);
    }

}
