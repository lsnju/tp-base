package com.lsnju.tpbase.daemon.monitor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.lsnju.base.model.BaseMo;
import com.lsnju.base.util.JsonUtils;
import com.lsnju.tpbase.daemon.AbstractNewTask;
import com.lsnju.tpbase.log.DigestConstants;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ls
 * @since 2022/1/23 12:06
 * @version V1.0
 */
public class TpMonitorTask extends AbstractNewTask {

    private static final Logger DIGEST_LOGGER = LoggerFactory.getLogger(DigestConstants.TP_TP_MO_DIGEST);

    private final List<TaskExecutor> threadPools;

    public TpMonitorTask(List<TaskExecutor> threadPools) {
        this.threadPools = threadPools == null ? Collections.emptyList() : threadPools;
        Objects.requireNonNull(this.threadPools);
    }

    @Override
    protected void execute() {
        log.debug("TpMonitorTask.execute {}", threadPools.size());
        for (TaskExecutor item : threadPools) {
            String msg = getDigestMsg(item);
            if (msg == null) {
                continue;
            }
            DIGEST_LOGGER.info(msg);
        }
    }

    private String getDigestMsg(TaskExecutor item) {
        if (item instanceof ThreadPoolTaskExecutor) {
            return getDigestMsgForThreadPoolTaskExecutor((ThreadPoolTaskExecutor) item);
        }
        if (item instanceof ThreadPoolTaskScheduler) {
            return getDigestMsgForThreadPoolTaskScheduler((ThreadPoolTaskScheduler) item);
        }
        return null;
    }

    private String getDigestMsgForThreadPoolTaskExecutor(ThreadPoolTaskExecutor item) {
        return JsonUtils.toJson(new TpMoInfoVo(item));
    }

    private String getDigestMsgForThreadPoolTaskScheduler(ThreadPoolTaskScheduler item) {
        return JsonUtils.toJson(new TpMoInfoVo(item));
    }

    @Getter
    @Setter
    public static class TpMoInfoVo extends BaseMo {
        private int core;
        private int max;
        private int size;
        private int act;
        private int large;
        private int queue;
        private int cap;
        private long total;
        private long complete;
        private String name;

        public TpMoInfoVo(ThreadPoolTaskExecutor item) {
            this.name = item.getThreadNamePrefix();
            this.core = item.getCorePoolSize();
            this.max = item.getMaxPoolSize();
            this.size = item.getPoolSize();
            this.act = item.getActiveCount();
            ThreadPoolExecutor executor = item.getThreadPoolExecutor();
            this.large = executor.getLargestPoolSize();
            this.complete = executor.getCompletedTaskCount();
            this.total = executor.getTaskCount();
            BlockingQueue<Runnable> queue = executor.getQueue();
            if (queue != null) {
                this.queue = queue.size();
                this.cap = queue.remainingCapacity();
            }
        }

        public TpMoInfoVo(ThreadPoolTaskScheduler item) {
            this.name = item.getThreadNamePrefix();
            this.size = item.getPoolSize();
            this.act = item.getActiveCount();
            ScheduledThreadPoolExecutor executor = item.getScheduledThreadPoolExecutor();
            this.core = executor.getCorePoolSize();
            this.max = executor.getMaximumPoolSize();
            this.large = executor.getLargestPoolSize();
            this.complete = executor.getCompletedTaskCount();
            this.total = executor.getTaskCount();
            BlockingQueue<Runnable> queue = executor.getQueue();
            if (queue != null) {
                this.queue = queue.size();
                this.cap = queue.remainingCapacity();
            }
        }
    }

}
