package com.lsnju.tpbase.daemon.monitor;

import java.util.Collection;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.annotation.PostConstruct;
import lombok.Setter;

/**
 *
 * @author ls
 * @since 2022/1/22 15:15
 * @version V1.0
 */
public class HikariCpsMonitorTask extends AbstractHikariCpMonitorTask {

    @Setter
    @Autowired
    private Collection<HikariDataSource> dataSources;

    @PostConstruct
    public void setup() {
        Objects.requireNonNull(dataSources);
        log.debug("{}", dataSources);
    }

    @Override
    protected void execute() {
        log.debug("HikariCpsMonitorTask.execute");
        if (CollectionUtils.isEmpty(dataSources)) {
            log.info("HikariCpsMonitorTask dataSources is empty. {}", dataSources);
            return;
        }
        for (HikariDataSource ds : dataSources) {
            logInfo(ds);
        }
    }

}
