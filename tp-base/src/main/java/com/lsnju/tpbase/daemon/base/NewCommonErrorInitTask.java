package com.lsnju.tpbase.daemon.base;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import com.lsnju.tpbase.daemon.AbstractNewTask;

/**
 *
 * @author ls
 * @since 2022/1/22 16:58
 * @version V1.0
 */
public class NewCommonErrorInitTask extends AbstractNewTask {

    @Value("${quartz.task}")
    private String taskStatus;

    @Override
    protected void execute() {
        log.error("init");
        log.warn("init");
    }

    @PostConstruct
    public void setup() {
        log.info("taskStatus = {}", taskStatus);
    }

}
