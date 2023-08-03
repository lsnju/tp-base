package com.lsnju.tpbase.web.controller.monitor.vo;

import com.lsnju.base.model.BaseMo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ls
 * @since 2023-08-03 12:56:34
 * @version V1.0
 */
@Getter
@Setter
public class TpInfoVo extends BaseMo {

    private String name;
    private int corePoolSize;
    private int maxPoolSize;
    private int poolSize;
    private int largestPoolSize;
    private int activeCount;
    private long keepAliveSeconds;
    private long completedTaskCount;
    private long taskCount;
    private int queueSize;
    private int remainingCapacity;

}
