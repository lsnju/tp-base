package com.lsnju.tpbase.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.lsnju.tpbase.web.filter.RequestId;
import com.lsnju.base.util.UUIDGenerator;

/**
 *
 * @author ls
 * @since 2022/1/22 16:55
 * @version V1.0
 */
public abstract class AbstractNewTask implements Runnable {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        try {
            MDC.put(RequestId.MDC_REQ_ID, UUIDGenerator.getSUID());
            execute();
        } finally {
            MDC.remove(RequestId.MDC_REQ_ID);
        }
    }

    protected abstract void execute();

}
