package com.lsnju.tpbase.web.controller.monitor;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lsnju.tpbase.util.VersionConfig;
import com.lsnju.tpbase.web.controller.monitor.vo.ThreadPoolStatusVo;
import com.lsnju.tpbase.web.controller.monitor.vo.TpInfoVo;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2021/11/3 8:59
 * @version V1.0
 */
@Slf4j
@Setter
@RestController
public class TpThreadPoolController {

    @Autowired(required = false)
    private List<ThreadPoolTaskExecutor> threadPools;
    @Autowired(required = false)
    private List<TaskScheduler> schedulerList;

    @GetMapping(path = "${tp.sys.mo.base-path}/tp.json")
    public ThreadPoolStatusVo show() {
        log.debug("{}", threadPools);
        log.debug("{}", schedulerList);
        final ThreadPoolStatusVo ret = new ThreadPoolStatusVo();
        ret.setHostname(VersionConfig.getHostname());
        ret.setSuccess(true);
        if (threadPools != null) {
            ret.setTpInfos(threadPools.stream().map(this::convert).collect(Collectors.toList()));
        }
        if (schedulerList != null) {
            ret.setScheduler(schedulerList.stream().map(this::convert).collect(Collectors.toList()));
        }
        return ret;
    }

    private TpInfoVo convert(ThreadPoolTaskExecutor item) {
        if (item == null) {
            return null;
        }
        final TpInfoVo vo = new TpInfoVo();
        vo.setName(item.getThreadNamePrefix());
        vo.setCorePoolSize(item.getCorePoolSize());
        vo.setMaxPoolSize(item.getMaxPoolSize());
        vo.setPoolSize(item.getPoolSize());
        vo.setActiveCount(item.getActiveCount());
        vo.setKeepAliveSeconds(item.getKeepAliveSeconds());

        final ThreadPoolExecutor executor = item.getThreadPoolExecutor();
        log.debug("{}", executor.getRejectedExecutionHandler());
        vo.setCompletedTaskCount(executor.getCompletedTaskCount());
        vo.setTaskCount(executor.getTaskCount());
        vo.setLargestPoolSize(executor.getLargestPoolSize());
        vo.setQueueSize(executor.getQueue().size());
        vo.setRemainingCapacity(executor.getQueue().remainingCapacity());
        return vo;
    }

    private TpInfoVo convert(TaskScheduler taskScheduler) {
        if (taskScheduler == null) {
            return null;
        }
        if (taskScheduler instanceof ThreadPoolTaskScheduler) {
            ThreadPoolTaskScheduler item = (ThreadPoolTaskScheduler) taskScheduler;
            final TpInfoVo vo = new TpInfoVo();
            vo.setName(item.getThreadNamePrefix());
            vo.setPoolSize(item.getPoolSize());
            vo.setActiveCount(item.getActiveCount());

            final ScheduledThreadPoolExecutor executor = item.getScheduledThreadPoolExecutor();
            log.debug("{}", executor.getRejectedExecutionHandler());
            vo.setCompletedTaskCount(executor.getCompletedTaskCount());
            vo.setTaskCount(executor.getTaskCount());
            vo.setLargestPoolSize(executor.getLargestPoolSize());
            vo.setQueueSize(executor.getQueue().size());
            vo.setRemainingCapacity(executor.getQueue().remainingCapacity());

            vo.setKeepAliveSeconds(executor.getKeepAliveTime(TimeUnit.SECONDS));
            vo.setCorePoolSize(executor.getCorePoolSize());
            vo.setMaxPoolSize(executor.getMaximumPoolSize());
            return vo;
        }
        final TpInfoVo vo = new TpInfoVo();
        vo.setName(taskScheduler.getClass().getName());
        return vo;
    }

}
