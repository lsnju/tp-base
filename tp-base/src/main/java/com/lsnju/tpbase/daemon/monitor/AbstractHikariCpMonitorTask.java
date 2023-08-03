package com.lsnju.tpbase.daemon.monitor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.DirectFieldAccessor;

import com.lsnju.tpbase.daemon.AbstractNewTask;
import com.lsnju.tpbase.log.DigestConstants;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;


/**
 *
 * @author ls
 * @since 2022/1/22 15:24
 * @version V1.0
 */
public abstract class AbstractHikariCpMonitorTask extends AbstractNewTask {

    private static final Logger DIGEST_LOGGER = LoggerFactory.getLogger(DigestConstants.TP_CP_MO_DIGEST);

    // [总连接数.活跃连接数,空闲连接数,等待连接线程数] [使用率（百分比]
    private static final String CP_MSG_TEMPLATE = "[%d,%d,%d,%d] [%.1f]";
    // [poolName,min,max]
    private static final String CP_META_TEMPLATE = "[%s,%d,%d]";

    protected void logInfo(HikariDataSource dataSource) {
        DIGEST_LOGGER.info("{} {}", getMetaInfo(dataSource), getCpInfo(dataSource));
    }

    protected HikariPool getHikariPool(HikariDataSource dataSource) {
        return (HikariPool) new DirectFieldAccessor(dataSource).getPropertyValue("pool");
    }

    protected String getMetaInfo(HikariDataSource dataSource) {
        String poolName = dataSource.getPoolName();
        int min = dataSource.getMinimumIdle();
        int max = dataSource.getMaximumPoolSize();
        return String.format(CP_META_TEMPLATE, poolName, min, max);
    }

    protected String getCpInfo(HikariDataSource dataSource) {
        final HikariPool pool = getHikariPool(dataSource);
        if (pool == null) {
            return StringUtils.EMPTY;
        }
        int active = pool.getActiveConnections();
        int idle = pool.getIdleConnections();
        int total = pool.getTotalConnections();
        int wait = pool.getThreadsAwaitingConnection();
        return String.format(CP_MSG_TEMPLATE, total, active, idle, wait, getUsage(total, active));
    }

    private double getUsage(int max, int active) {
        if (max < 0) {
            return -1;
        }
        if (active == 0) {
            return 0;
        }
        return active * 100.0 / max;
    }
}
