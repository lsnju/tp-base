package com.lsnju.tpbase.debug.env;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2021/4/7 12:39
 * @version V1.0
 */
@Slf4j
public class ApplicationPreparedEventListener extends AbstractEnvShow implements ApplicationListener<ApplicationPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        log.info("ApplicationPreparedEventListener");
        showEnv(log, event.getApplicationContext().getEnvironment());
    }
}
