package com.lsnju.tpbase.autoconfigure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import jakarta.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.triggers.AbstractTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.CustomizableThreadCreator;

import com.lsnju.tpbase.TpConstants;
import com.lsnju.tpbase.daemon.base.CommonErrorInitTask;
import com.lsnju.tpbase.daemon.base.NewCommonErrorInitTask;
import com.lsnju.tpbase.daemon.monitor.HikariCpsMonitorTask;
import com.lsnju.tpbase.daemon.monitor.TpMonitorTask;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2022/8/23 10:39
 * @version V1.0
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
class TpTaskConfiguration {

    private static final String TAG = TpConstants.PREFIX;

    @Configuration
    @ConditionalOnClass(name = {"org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor"})
    public static class TpMonitorConfig {

        public static final String TP_INFO_TEMPLATE = "--> %-16s [core=%d,size=%d,max=%d]  %s";

        @Bean
        @ConditionalOnMissingBean
        public TpMonitorTask tpMonitorTask(@Autowired List<TaskExecutor> threadPools) {
            log.debug("{} tpMonitorTask {}", TAG, threadPools);
            if (CollectionUtils.isEmpty(threadPools)) {
                return new TpMonitorTask(Collections.emptyList());
            }
            for (TaskExecutor item : threadPools) {
                log.info(getTaskExecutorInfo(item));
            }
            return new TpMonitorTask(threadPools);
        }

        private String getTaskExecutorInfo(TaskExecutor e) {
            if (e instanceof ThreadPoolTaskExecutor) {
                return showThreadPoolTaskExecutor((ThreadPoolTaskExecutor) e);
            }
            if (e instanceof ThreadPoolTaskScheduler) {
                return showThreadPoolTaskScheduler((ThreadPoolTaskScheduler) e);
            }
            return e.toString();
        }

        private String showThreadPoolTaskExecutor(ThreadPoolTaskExecutor item) {
            return String.format(TP_INFO_TEMPLATE, getTpName(item),
                item.getCorePoolSize(), item.getPoolSize(), item.getMaxPoolSize(), item);
        }

        private String showThreadPoolTaskScheduler(ThreadPoolTaskScheduler item) {
            final ScheduledThreadPoolExecutor executor = item.getScheduledThreadPoolExecutor();
            return String.format(TP_INFO_TEMPLATE, getTpName(item),
                executor.getCorePoolSize(), executor.getPoolSize(), executor.getMaximumPoolSize(), item);
        }

        private String getTpName(CustomizableThreadCreator tp) {
            return StringUtils.substring(tp.getThreadNamePrefix(), 0, -1);
        }
    }

    @Configuration
    @ConditionalOnClass(name = "com.zaxxer.hikari.HikariDataSource")
    public static class HikariCpsMonitorConfig {
        @Bean
        @ConditionalOnMissingBean
        public HikariCpsMonitorTask hikariCpsMonitorTask() {
            log.debug("{} hikariCpsMonitorTask", TAG);
            return new HikariCpsMonitorTask();
        }
    }

    @Configuration
    @AutoConfigureAfter(OldCommonErrorInitConfig.class)
    @ConditionalOnMissingBean(CommonErrorInitTask.class)
    public static class NewCommonErrorInitConfig {
        @Bean
        @ConditionalOnMissingBean
        public NewCommonErrorInitTask newCommonErrorInitTask() {
            log.debug("{} newCommonErrorInitTask", TAG);
            return new NewCommonErrorInitTask();
        }
    }

    @Configuration
    public static class OldCommonErrorInitConfig {
        @Autowired(required = false)
        private CommonErrorInitTask commonErrorInitTask;

        @PostConstruct
        public void setup() {
            log.info("CommonErrorInitTask = {}", commonErrorInitTask);
        }
    }

    @Configuration
    @ConditionalOnClass(name = {"org.quartz.Scheduler", "org.springframework.scheduling.quartz.SchedulerFactoryBean"})
    public static class OldSchedulerFactoryConfig {
        @Autowired(required = false)
        @Qualifier("org.springframework.scheduling.quartz.SchedulerFactoryBean#0")
        private Scheduler scheduler;
        @Autowired
        private ApplicationContext context;

        @PostConstruct
        public void setup() {
            log.info("SchedulerFactory.names = {}", Arrays.toString(context.getBeanNamesForType(SchedulerFactoryBean.class)));
            log.info("Scheduler.names        = {}", Arrays.toString(context.getBeanNamesForType(Scheduler.class)));
            log.info("oldScheduler           = {}", scheduler);
        }

        public boolean isXmlConfig() {
            return scheduler != null;
        }
    }

    @Configuration
    @ConditionalOnClass(name = {"org.quartz.Trigger", "org.springframework.scheduling.quartz.SchedulerFactoryBean"})
    @AutoConfigureAfter(value = {TpBaseConfiguration.TpThreadPoolConfig.class})
    public static class TpQuartzTaskConfig {
        @Bean
        SchedulerFactoryBeanCustomizer tpSchedulerFactoryBeanCustomizer(@Qualifier("tpForTask") @Autowired(required = false)
                                                                        ThreadPoolTaskExecutor tpForTask,
                                                                        @Autowired ThreadPoolTaskExecutor tpThreadPool,
                                                                        @Autowired Trigger[] triggers) {
            show(tpForTask, tpThreadPool, triggers);
            final Executor executor = tpForTask != null ? tpForTask : tpThreadPool;
            return schedulerFactoryBean -> {
                log.info("SchedulerFactoryBeanCustomizer.TaskExecutor = {}", executor);
                schedulerFactoryBean.setTaskExecutor(executor);
            };
        }

        private void show(ThreadPoolTaskExecutor tpForTask, ThreadPoolTaskExecutor tpThreadPool, Trigger[] triggers) {
            log.info("--> trigger.size = {}", triggers.length);
            log.info("--> tpForTask  = {}", tpForTask);
            log.info("--> tpDefault  = {}", tpThreadPool);
            for (Trigger t : triggers) {
                log.debug("[trigger] {}", getTriggerInfo(t));
            }
        }

        private String getTriggerInfo(final Trigger trigger) {
            if (trigger instanceof AbstractTrigger) {
                final AbstractTrigger<?> at = (AbstractTrigger<?>) trigger;
                return String.format("%-40s = [%s]", at.getName(), getDesc(at));
            }
            return trigger.getClass().getName();
        }

        private String getDesc(final AbstractTrigger<?> trigger) {
            if (trigger instanceof CronTrigger) {
                return ((CronTrigger) trigger).getCronExpression();
            }
            return trigger.getClass().getName();
        }
    }

}
