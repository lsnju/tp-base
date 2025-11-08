package com.lsnju.tpbase.debug.env;

import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author lis614
 * @since 2025-11-08 11:27:18
 * @version V1.0
 */
public class TpStartedEventListener implements ApplicationListener<ApplicationStartedEvent> {

    private final Logger log;
    private final Level level;

    public TpStartedEventListener(Logger log) {
        this(log, Level.INFO);
    }

    public TpStartedEventListener(Logger log, Level level) {
        this.log = log;
        this.level = level;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        ConfigurableApplicationContext ctx = event.getApplicationContext();
        final String appName = ctx.getEnvironment().getProperty("spring.application.name");
        final String port = ctx.getEnvironment().getProperty("server.port");
        if (level == Level.ERROR) {
            log.error("{} : -------------------------------------------------------", appName);
            log.error("{} : start success (port = {})(cost = {}ms)...", appName, port, event.getTimeTaken().toMillis());
            log.error("{} : -------------------------------------------------------", appName);
        } else if (level == Level.WARN) {
            log.warn("{} : -------------------------------------------------------", appName);
            log.warn("{} : start success (port = {})(cost = {}ms)...", appName, port, event.getTimeTaken().toMillis());
            log.warn("{} : -------------------------------------------------------", appName);
        } else {
            log.info("{} : -------------------------------------------------------", appName);
            log.info("{} : start success (port = {})(cost = {}ms)...", appName, port, event.getTimeTaken().toMillis());
            log.info("{} : -------------------------------------------------------", appName);
        }
    }

}
