package com.lsnju.tpbase.daemon.base;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import com.lsnju.tpbase.daemon.AbstractNewTask;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ls
 * @since 2022/1/22 16:58
 * @version V1.0
 */
@Getter
@Setter
public class NewCommonErrorInitTask extends AbstractNewTask {

    @Value("${tp.quartz.enable:false}")
    private boolean taskStatus;

    @Override
    protected void execute() {
        log.error("init");
        log.warn("init");
    }

    @PostConstruct
    public void setup() {
        log.info("tp.quartz.toggle = {}", taskStatus);
    }

}
