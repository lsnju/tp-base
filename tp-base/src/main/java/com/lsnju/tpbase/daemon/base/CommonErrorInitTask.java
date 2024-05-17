package com.lsnju.tpbase.daemon.base;

import javax.annotation.PostConstruct;

import com.lsnju.tpbase.daemon.AbstractTask;

/**
 * @author lisong
 * @since 2020/2/20 19:38
 * @version V1.0
 */
public class CommonErrorInitTask extends AbstractTask {
    @Override
    public void execute() {
        log.error("init");
        log.warn("init");
    }

    @PostConstruct
    public void setup() {
        log.info("quartz.taskStatus = {}", getTaskStatus());
    }
}
