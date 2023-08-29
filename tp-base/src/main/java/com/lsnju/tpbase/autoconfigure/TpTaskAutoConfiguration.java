package com.lsnju.tpbase.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.lsnju.tpbase.TpConstants;
import com.lsnju.tpbase.config.prop.TpTaskConfigProperties;
import com.lsnju.tpbase.daemon.base.NewCommonErrorInitTask;
import com.lsnju.tpbase.daemon.monitor.HikariCpsMonitorTask;
import com.lsnju.tpbase.daemon.monitor.TpMonitorTask;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2022/1/22 15:03
 * @version V1.0
 */
@Slf4j
@EnableConfigurationProperties({TpTaskConfigProperties.class})
@ConditionalOnProperty(name = "tp.task.enable", matchIfMissing = true)
@Configuration
@EnableScheduling
@Import({
    TpTaskConfiguration.TpMonitorConfig.class,
    TpTaskConfiguration.HikariCpsMonitorConfig.class,
    TpTaskConfiguration.NewCommonErrorInitConfig.class,
    TpTaskConfiguration.OldCommonErrorInitConfig.class,
    TpTaskConfiguration.OldSchedulerFactoryConfig.class,
    TpTaskConfiguration.TpQuartzTaskConfig.class,
})
public class TpTaskAutoConfiguration {

    @Bean
    public SchedulingConfigurer tpScheduleTaskConfig(final TpTaskConfigProperties tpTaskConfigProperties) {
        log.debug("{} tpScheduleTaskConfig", TpConstants.PREFIX);
        return new SchedulingConfigurer() {
            @Autowired(required = false)
            private HikariCpsMonitorTask hikariCpsMonitorTask;
            @Autowired(required = false)
            private NewCommonErrorInitTask newCommonErrorInitTask;
            @Autowired(required = false)
            private TpMonitorTask tpMonitorTask;

            @Override
            public void configureTasks(@NonNull ScheduledTaskRegistrar taskRegistrar) {
                if (hikariCpsMonitorTask != null && tpTaskConfigProperties.isCpMoEnable()) {
                    final String cron = tpTaskConfigProperties.getCpMoCron();
                    taskRegistrar.addCronTask(hikariCpsMonitorTask, cron);
                    logMsg("hikariCpsMonitorTask", cron);
                }
                if (newCommonErrorInitTask != null && tpTaskConfigProperties.isCommonErrorEnable()) {
                    final String cron = tpTaskConfigProperties.getCommonErrorCron();
                    taskRegistrar.addCronTask(newCommonErrorInitTask, cron);
                    logMsg("newCommonErrorInitTask", cron);
                }
                if (tpMonitorTask != null && tpTaskConfigProperties.isTpMoEnable()) {
                    final String cron = tpTaskConfigProperties.getTpMoCron();
                    taskRegistrar.addCronTask(tpMonitorTask, cron);
                    logMsg("tpMonitorTask", cron);
                }
            }

            private void logMsg(String name, String cron) {
                if (log.isInfoEnabled()) {
                    log.info(String.format("[new-task] %-25s = [%s]", name, cron));
                }
            }
        };
    }

}
