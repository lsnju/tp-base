package com.lsnju.tpbase.daemon.monitor;

import java.util.Collection;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import com.zaxxer.hikari.HikariDataSource;

/**
 *
 * @author ls
 * @since 2022/1/22 15:15
 * @version V1.0
 */
public class HikariCpsMonitorTask extends AbstractHikariCpMonitorTask implements InitializingBean {

    private final Collection<HikariDataSource> dataSources;

    public HikariCpsMonitorTask(Collection<HikariDataSource> dataSources) {
        this.dataSources = dataSources;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
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
