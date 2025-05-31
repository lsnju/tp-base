package com.lsnju.tpbase.daemon.base;

import org.springframework.beans.factory.InitializingBean;

import com.lsnju.tpbase.daemon.AbstractTask;

/**
 * @author lisong
 * @since 2020/2/20 19:38
 * @version V1.0
 */
public class CommonErrorInitTask extends AbstractTask implements InitializingBean {
    @Override
    protected void execute() {
        log.error("init");
        log.warn("init");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("tp.quartz.toggle = {}", isEnableQuartzTask());
    }

}
