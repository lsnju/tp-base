package com.lsnju.tpbase.debug.env;

import org.slf4j.Logger;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

/**
 *
 * @author lis614
 * @since 2025/2/13 13:39
 * @version V1.0
 */
public class TpStartFailEventListener implements ApplicationListener<ApplicationFailedEvent> {

    private final Logger log;

    public TpStartFailEventListener(Logger log) {
        this.log = log;
    }

    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
        Throwable exception = event.getException();
        log.error("ApplicationFailedEvent", exception);
    }

}
