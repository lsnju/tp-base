package com.lsnju.tpbase.daemon.monitor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.lsnju.tpbase.daemon.AbstractNewTask;
import com.lsnju.tpbase.log.DigestConstants;

/**
 *
 * @author ls
 * @since 2022/1/23 15:02
 * @version V1.0
 */
public class TsMonitorTask extends AbstractNewTask {

    private static final Logger DIGEST_LOGGER = LoggerFactory.getLogger(DigestConstants.TP_TS_MO_DIGEST);

    private final List<ThreadPoolTaskScheduler> threadPools;

    public TsMonitorTask(List<ThreadPoolTaskScheduler> threadPools) {
        this.threadPools = threadPools;
    }

    @Override
    protected void execute() {
        log.debug("TpMonitorTask.execute {}", threadPools != null ? threadPools.size() : 0);
        if (threadPools == null) {
            return;
        }
        for (ThreadPoolTaskScheduler item : threadPools) {
            DIGEST_LOGGER.info(getDigestMsg(item));
        }
    }

    private String getDigestMsg(ThreadPoolTaskScheduler item) {
        log.info("{}", item.getScheduledThreadPoolExecutor());
        return String.format("[%s,%s,%s]", item.getPoolSize(), item.getActiveCount(), item.getThreadNamePrefix());
    }

}
