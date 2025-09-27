package com.lsnju.tpbase.daemon.monitor;

import java.util.Objects;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.jdbc.metadata.HikariDataSourcePoolMetadata;

import com.zaxxer.hikari.HikariDataSource;

/**
 *
 * @author lisong
 * @since 2022/1/21 11:03
 * @version V1.0
 */
public class HikariCpMonitorTask extends AbstractHikariCpMonitorTask implements InitializingBean {

    private final HikariDataSource dataSource;

    public HikariCpMonitorTask(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
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
