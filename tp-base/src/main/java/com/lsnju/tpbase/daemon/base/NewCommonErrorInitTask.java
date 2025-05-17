package com.lsnju.tpbase.daemon.base;

import org.springframework.beans.factory.InitializingBean;
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
public class NewCommonErrorInitTask extends AbstractNewTask implements InitializingBean {

    @Value("${tp.quartz.enable:false}")
    private boolean taskStatus;

    @Override
    protected void execute() {
        log.error("init");
        log.warn("init");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("tp.quartz.toggle = {}", taskStatus);
    }

}
